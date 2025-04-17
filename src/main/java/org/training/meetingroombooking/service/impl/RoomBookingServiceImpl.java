package org.training.meetingroombooking.service.impl;

import jakarta.mail.MessagingException;
import jakarta.persistence.criteria.Join;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.training.meetingroombooking.entity.dto.NotificationDTO;
import org.training.meetingroombooking.entity.dto.RoomBookingDTO;
import org.training.meetingroombooking.entity.enums.BookingStatus;
import org.training.meetingroombooking.entity.enums.ErrorCode;
import org.training.meetingroombooking.entity.enums.NotificationType;
import org.training.meetingroombooking.entity.mapper.RoomBookingMapper;
import org.training.meetingroombooking.entity.models.Room;
import org.training.meetingroombooking.entity.models.RoomBooking;
import org.training.meetingroombooking.entity.models.User;
import org.training.meetingroombooking.exception.AppEx;
import org.training.meetingroombooking.repository.RoomBookingRepository;
import org.training.meetingroombooking.repository.RoomRepository;
import org.training.meetingroombooking.repository.UserRepository;
import org.training.meetingroombooking.service.EmailService;
import org.training.meetingroombooking.service.NotificationService;
import org.training.meetingroombooking.service.RoomBookingService;

@Slf4j
@Service
public class RoomBookingServiceImpl implements RoomBookingService {

  private final RoomBookingRepository roomBookingRepository;
  private final RoomBookingMapper roomBookingMapper;
  private final EmailService emailService;
  private final RoomRepository roomRepository;
  private final UserRepository userRepository;
  private final NotificationService notificationService;

  public RoomBookingServiceImpl(
      RoomBookingRepository roomBookingRepository,
      RoomBookingMapper roomBookingMapper,
      EmailService emailService,
      RoomRepository roomRepository,
      UserRepository userRepository,
      NotificationService notificationService) {
    this.roomBookingRepository = roomBookingRepository;
    this.roomBookingMapper = roomBookingMapper;
    this.emailService = emailService;
    this.roomRepository = roomRepository;
    this.userRepository = userRepository;
    this.notificationService = notificationService;
  }

  public RoomBookingDTO create(RoomBookingDTO dto) {
    Room room =
        roomRepository
            .findById(dto.getRoomId())
            .orElseThrow(() -> new AppEx(ErrorCode.ROOM_NOT_FOUND));
    User user =
        userRepository
            .findById(dto.getBookedById())
            .orElseThrow(() -> new AppEx(ErrorCode.USER_NOT_FOUND));
    boolean isOverlap =
        roomBookingRepository.existsByRoomAndTimeOverlap(
            dto.getRoomId(), dto.getStartTime(), dto.getEndTime());
    if (isOverlap) {
      throw new AppEx(ErrorCode.ALREADY_BOOKED);
    }
    RoomBooking roomBooking = roomBookingMapper.toEntity(dto);
    roomBooking.setRoom(room);
    roomBooking.setBookedBy(user);
    RoomBooking savedRoomBooking = roomBookingRepository.save(roomBooking);
    RoomBookingDTO savedDTO = roomBookingMapper.toDTO(savedRoomBooking);
    // Gửi email xác nhận
    emailService.sendRoomBookingConfirmationEmail(savedDTO);
    // Tạo thông báo
    NotificationDTO notificationDTO =
        NotificationDTO.builder()
            .content(
                "Room '"
                    + room.getRoomName()
                    + "' booking successfully from "
                    + dto.getStartTime()
                    + " to "
                    + dto.getEndTime())
            .type(NotificationType.INFO)
            .userId(user.getUserId())
            .build();
    notificationService.create(notificationDTO);
    return savedDTO;
  }

  /**
   * Lấy danh sách RoomBooking của người dùng hiện hành theo các tiêu chí: - roomName: tìm kiếm theo
   * tên phòng (không phân biệt chữ hoa chữ thường) - fromTime: thời gian bắt đầu (startTime) từ -
   * toTime: thời gian kết thúc (endTime) đến - status: trạng thái đặt phòng - Phân trang theo thứ
   * tự giảm dần của bookingId
   */
  public Page<RoomBookingDTO> searchMyBookings(
      String roomName,
      LocalDateTime fromTime,
      LocalDateTime toTime,
      BookingStatus status,
      int page,
      int size) {
    String currentUserName = SecurityContextHolder.getContext().getAuthentication().getName();
    Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "bookingId"));
    Specification<RoomBooking> spec =
        Specification.where(
            (root, query, cb) -> cb.equal(root.get("bookedBy").get("userName"), currentUserName));
    if (roomName != null && !roomName.isEmpty()) {
      spec =
          spec.and(
              (root, query, cb) -> {
                Join<RoomBooking, Room> roomJoin = root.join("room");
                return cb.like(
                    cb.lower(roomJoin.get("roomName")), "%" + roomName.toLowerCase() + "%");
              });
    }
    if (fromTime != null) {
      spec =
          spec.and((root, query, cb) -> cb.greaterThanOrEqualTo(root.get("startTime"), fromTime));
    }
    if (toTime != null) {
      spec = spec.and((root, query, cb) -> cb.lessThanOrEqualTo(root.get("endTime"), toTime));
    }
    if (status != null) {
      spec = spec.and((root, query, cb) -> cb.equal(root.get("status"), status));
    }
    Page<RoomBooking> bookingPage = roomBookingRepository.findAll(spec, pageable);
    return bookingPage.map(roomBookingMapper::toDTO);
  }

  public Page<RoomBookingDTO> searchBookings(
      String roomName,
      LocalDateTime fromTime,
      LocalDateTime toTime,
      BookingStatus status,
      String bookedByName,
      int page,
      int size) {
    Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "bookingId"));
    Specification<RoomBooking> spec = Specification.where(null);

    if (roomName != null && !roomName.isEmpty()) {
      spec =
          spec.and(
              (root, query, cb) -> {
                Join<RoomBooking, Room> roomJoin = root.join("room");
                return cb.like(
                    cb.lower(roomJoin.get("roomName")), "%" + roomName.toLowerCase() + "%");
              });
    }
    if (fromTime != null) {
      spec =
          spec.and((root, query, cb) -> cb.greaterThanOrEqualTo(root.get("startTime"), fromTime));
    }
    if (toTime != null) {
      spec = spec.and((root, query, cb) -> cb.lessThanOrEqualTo(root.get("endTime"), toTime));
    }
    if (status != null) {
      spec = spec.and((root, query, cb) -> cb.equal(root.get("status"), status));
    }
    // Sử dụng userName trong bookedBy vì nó độc nhất.
    if (bookedByName != null && !bookedByName.isEmpty()) {
      spec =
          spec.and(
              (root, query, cb) -> {
                Join<RoomBooking, User> userJoin = root.join("bookedBy");
                return cb.like(
                    cb.lower(userJoin.get("userName")), "%" + bookedByName.toLowerCase() + "%");
              });
    }

    Page<RoomBooking> bookingPage = roomBookingRepository.findAll(spec, pageable);
    return bookingPage.map(roomBookingMapper::toDTO);
  }

  public List<RoomBookingDTO> getAll() {
    List<RoomBooking> roomBookings = roomBookingRepository.findAll();
    return roomBookings.stream().map(roomBookingMapper::toDTO).collect(Collectors.toList());
  }

  public List<RoomBookingDTO> getBookingsByUserName(String userName) {
    List<RoomBooking> roomBookings = roomBookingRepository.findByBookedBy_UserName(userName);
    return roomBookings.stream().map(roomBookingMapper::toDTO).collect(Collectors.toList());
  }

  public RoomBookingDTO update(Long bookingId, RoomBookingDTO dto) {
    RoomBooking roomBooking =
        roomBookingRepository
            .findById(bookingId)
            .orElseThrow(() -> new AppEx(ErrorCode.ROOM_BOOKING_NOT_FOUND));
    roomBookingMapper.updateEntity(roomBooking, dto);

    if (dto.getRoomId() != null) {
      Room room =
          roomRepository
              .findById(dto.getRoomId())
              .orElseThrow(() -> new AppEx(ErrorCode.ROOM_NOT_FOUND));
      roomBooking.setRoom(room);
    }
    if (dto.getBookedById() != null) {
      User user =
          userRepository
              .findById(dto.getBookedById())
              .orElseThrow(() -> new AppEx(ErrorCode.USER_NOT_FOUND));
      roomBooking.setBookedBy(user);
    }
    if (dto.getStartTime() != null && dto.getEndTime() != null) {
      // Kiểm tra hợp lệ: startTime không được sau endTime
      if (dto.getStartTime().isAfter(dto.getEndTime())) {
        throw new AppEx(ErrorCode.INVALID_TIME_RANGE);
      }
      boolean isOverlap =
          roomBookingRepository.existsByRoomAndTimeOverlapExcludingId(
              dto.getRoomId(), dto.getStartTime(), dto.getEndTime(), bookingId);
      if (isOverlap) {
        throw new AppEx(ErrorCode.ALREADY_BOOKED);
      }
      roomBooking.setStartTime(dto.getStartTime());
      roomBooking.setEndTime(dto.getEndTime());
    }
    if (dto.getStatus() != null) {
      roomBooking.setStatus(dto.getStatus());
    }
    RoomBooking updatedRoomBooking = roomBookingRepository.save(roomBooking);
    RoomBookingDTO updatedDTO = roomBookingMapper.toDTO(updatedRoomBooking);

    User bookingUser = updatedRoomBooking.getBookedBy();
    NotificationDTO notificationDTO =
        NotificationDTO.builder()
            .content(
                "Your booking for room '"
                    + updatedRoomBooking.getRoom().getRoomName()
                    + "' has been updated. New time: "
                    + updatedRoomBooking.getStartTime()
                    + " - "
                    + updatedRoomBooking.getEndTime())
            .type(NotificationType.INFO)
            .userId(bookingUser.getUserId())
            .build();
    notificationService.create(notificationDTO);

    return updatedDTO;
  }

  public void delete(Long bookingId) {
    RoomBooking roomBooking =
        roomBookingRepository
            .findById(bookingId)
            .orElseThrow(() -> new AppEx(ErrorCode.ROOM_BOOKING_NOT_FOUND));

    String roomName = roomBooking.getRoom().getRoomName();
    LocalDateTime startTime = roomBooking.getStartTime();
    LocalDateTime endTime = roomBooking.getEndTime();
    Long userId = roomBooking.getBookedBy().getUserId();
    roomBookingRepository.deleteById(bookingId);
    NotificationDTO notificationDTO =
        NotificationDTO.builder()
            .content(
                "Your booking for room '"
                    + roomName
                    + "' from "
                    + startTime
                    + " to "
                    + endTime
                    + " has been deleted.")
            .type(NotificationType.WARNING)
            .userId(userId)
            .build();
    notificationService.create(notificationDTO);
  }

  public ByteArrayOutputStream exportBookingsToExcel() throws IOException {
    List<RoomBookingDTO> bookings = getAll();
    try (Workbook workbook = new XSSFWorkbook();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
      Sheet sheet = workbook.createSheet("Bookings");
      // Tạo hàng tiêu đề
      Row headerRow = sheet.createRow(0);
      String[] headers = {
        "Room ID", "Room Name", "Booked By", "Start Time", "End Time", "Status", "Description"
      };
      for (int i = 0; i < headers.length; i++) {
        Cell cell = headerRow.createCell(i);
        cell.setCellValue(headers[i]);
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);
        cell.setCellStyle(style);
      }
      int rowNum = 1;
      for (RoomBookingDTO booking : bookings) {
        Row row = sheet.createRow(rowNum++);
        if (booking.getRoomId() != null) {
          row.createCell(0).setCellValue(booking.getRoomId());
        } else {
          row.createCell(0).setCellValue("N/A");
        }
        row.createCell(1)
            .setCellValue(booking.getRoomName() != null ? booking.getRoomName() : "N/A");
        row.createCell(2)
            .setCellValue(booking.getUserName() != null ? booking.getUserName() : "N/A");
        row.createCell(3)
            .setCellValue(
                booking.getStartTime() != null ? booking.getStartTime().toString() : "N/A");
        row.createCell(4)
            .setCellValue(booking.getEndTime() != null ? booking.getEndTime().toString() : "N/A");
        row.createCell(5)
            .setCellValue(booking.getStatus() != null ? booking.getStatus().toString() : "N/A");
        row.createCell(6)
            .setCellValue(booking.getDescription() != null ? booking.getDescription() : "N/A");
      }
      for (int i = 0; i < headers.length; i++) {
        sheet.autoSizeColumn(i);
      }
      workbook.write(outputStream);
      return outputStream;
    }
  }

  @Scheduled(fixedRate = 3600000) // 1h
  @Transactional
  public void sendMeetingReminderEmails() {
    LocalDateTime now = LocalDateTime.now();
    LocalDateTime HoursLater = now.plusHours(1);
    List<RoomBooking> upcomingMeetings = roomBookingRepository.findMeetingsBetween(now, HoursLater);
    for (RoomBooking booking : upcomingMeetings) {
      try {
        emailService.sendMeetingReminderEmail(
            booking.getBookedBy().getEmail(),
            booking.getBookedBy().getUserName(),
            booking.getRoom().getRoomName(),
            booking.getStartTime().toString(),
            booking.getEndTime().toString());
      } catch (MessagingException e) {
        e.printStackTrace();
      }
    }
  }

  public List<RoomBookingDTO> getBookingsByRoomId(Long roomId) {
    Room room =
        roomRepository.findById(roomId).orElseThrow(() -> new AppEx(ErrorCode.ROOM_NOT_FOUND));
    List<RoomBooking> roomBookings = room.getBookings();
    if (roomBookings.isEmpty()) {
      return Collections.emptyList();
    }
    return roomBookings.stream()
        .filter(booking -> booking.getStatus() == BookingStatus.CONFIRMED)
        .map(roomBookingMapper::toDTO)
        .collect(Collectors.toList());
  }

  public List<RoomBookingDTO> getUpcomingBookings() {
    LocalDateTime now = LocalDateTime.now();
    List<RoomBooking> upcomingBookings =
        roomBookingRepository.findByStatusAndStartTimeAfter(BookingStatus.CONFIRMED, now);
    return upcomingBookings.stream().map(roomBookingMapper::toDTO).collect(Collectors.toList());
  }

  public List<RoomBookingDTO> getUpcomingBookingsByUserName(String userName) {
    LocalDateTime now = LocalDateTime.now();
    List<RoomBooking> upcomingBookings =
        roomBookingRepository.findByBookedBy_UserNameAndStatusAndStartTimeAfter(
            userName, BookingStatus.CONFIRMED, now);
    return upcomingBookings.stream().map(roomBookingMapper::toDTO).collect(Collectors.toList());
  }

  public List<RoomBookingDTO> getUpcomingBookingsByRoomId(Long roomId) {
    LocalDateTime now = LocalDateTime.now();
    List<RoomBooking> upcomingBookings =
        roomBookingRepository.findByRoom_roomIdAndStatusAndStartTimeAfter(
            roomId, BookingStatus.CONFIRMED, now);
    return upcomingBookings.stream().map(roomBookingMapper::toDTO).collect(Collectors.toList());
  }

  /**
   * Lấy danh sách các booking sắp tới (startTime > hiện tại) của người dùng hiện tại, có: -
   * roomName: tìm kiếm tên phòng (không phân biệt chữ hoa thường) - fromTime: thời gian bắt đầu từ
   * - toTime: thời gian kết thúc đến - Phân trang theo thứ tự giảm dần bookingId
   */
  public Page<RoomBookingDTO> getMyUpcomingBookings(
      String roomName, LocalDateTime fromTime, LocalDateTime toTime, int page, int size) {
    String currentUserName = SecurityContextHolder.getContext().getAuthentication().getName();
    LocalDateTime now = LocalDateTime.now();
    Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "bookingId"));
    Specification<RoomBooking> spec =
        Specification.where(
            (root, query, cb) ->
                cb.and(
                    cb.equal(root.get("bookedBy").get("userName"), currentUserName),
                    cb.equal(root.get("status"), BookingStatus.CONFIRMED),
                    cb.greaterThan(root.get("startTime"), now)));
    if (roomName != null && !roomName.isEmpty()) {
      spec =
          spec.and(
              (root, query, cb) -> {
                Join<RoomBooking, Room> roomJoin = root.join("room");
                return cb.like(
                    cb.lower(roomJoin.get("roomName")), "%" + roomName.toLowerCase() + "%");
              });
    }
    if (fromTime != null) {
      spec =
          spec.and((root, query, cb) -> cb.greaterThanOrEqualTo(root.get("startTime"), fromTime));
    }
    if (toTime != null) {
      spec = spec.and((root, query, cb) -> cb.lessThanOrEqualTo(root.get("endTime"), toTime));
    }
    Page<RoomBooking> resultPage = roomBookingRepository.findAll(spec, pageable);
    return resultPage.map(roomBookingMapper::toDTO);
  }

  public RoomBookingDTO cancelBooking(Long bookingId) {
    RoomBooking roomBooking =
        roomBookingRepository
            .findById(bookingId)
            .orElseThrow(() -> new AppEx(ErrorCode.ROOM_BOOKING_NOT_FOUND));
    if (roomBooking.getStatus() == BookingStatus.CANCELLED) {
      return roomBookingMapper.toDTO(roomBooking);
    }
    roomBooking.setStatus(BookingStatus.CANCELLED);
    RoomBooking cancelledBooking = roomBookingRepository.save(roomBooking);
    NotificationDTO notificationDTO =
        NotificationDTO.builder()
            .content(
                "Your booking for room '"
                    + cancelledBooking.getRoom().getRoomName()
                    + "' from "
                    + cancelledBooking.getStartTime()
                    + " to "
                    + cancelledBooking.getEndTime()
                    + " has been cancelled.")
            .type(NotificationType.WARNING)
            .userId(cancelledBooking.getBookedBy().getUserId())
            .build();
    notificationService.create(notificationDTO);
    return roomBookingMapper.toDTO(cancelledBooking);
  }

  @Transactional
  public void cancelMultipleBookings(List<Long> bookingIds) {
    if (bookingIds == null || bookingIds.isEmpty()) {
      throw new IllegalArgumentException("Booking ids list cannot be empty");
    }
    List<RoomBooking> bookings = roomBookingRepository.findAllById(bookingIds);
    if (bookings.size() != bookingIds.size()) {
      throw new AppEx(ErrorCode.BATCH_CANCELLATION_FAILED);
    }
    for (RoomBooking booking : bookings) {
      booking.setStatus(BookingStatus.CANCELLED);
    }
    roomBookingRepository.saveAll(bookings);
    for (RoomBooking booking : bookings) {
      NotificationDTO notificationDTO =
          NotificationDTO.builder()
              .content(
                  "Your booking for room '"
                      + booking.getRoom().getRoomName()
                      + "' from "
                      + booking.getStartTime()
                      + " to "
                      + booking.getEndTime()
                      + " has been cancelled.")
              .type(NotificationType.WARNING)
              .userId(booking.getBookedBy().getUserId())
              .build();
      notificationService.create(notificationDTO);
    }
  }
}
