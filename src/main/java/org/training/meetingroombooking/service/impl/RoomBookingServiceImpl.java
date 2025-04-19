package org.training.meetingroombooking.service.impl;

import jakarta.mail.MessagingException;
import jakarta.persistence.criteria.Join;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
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
@RequiredArgsConstructor
public class RoomBookingServiceImpl implements RoomBookingService {

  private static final long REMINDER_INTERVAL_MS = 3_600_000; // 1 hour

  private final RoomBookingRepository roomBookingRepo;
  private final RoomBookingMapper mapper;
  private final EmailService emailService;
  private final RoomRepository roomRepo;
  private final UserRepository userRepo;
  private final NotificationService notificationService;

  @Override
  public RoomBookingDTO create(RoomBookingDTO dto) {
    Room room =
        roomRepo.findById(dto.getRoomId()).orElseThrow(() -> new AppEx(ErrorCode.ROOM_NOT_FOUND));
    User user =
        userRepo
            .findById(dto.getBookedById())
            .orElseThrow(() -> new AppEx(ErrorCode.USER_NOT_FOUND));
    if (roomBookingRepo.existsByRoomAndTimeOverlap(
        dto.getRoomId(), dto.getStartTime(), dto.getEndTime())) {
      throw new AppEx(ErrorCode.ALREADY_BOOKED);
    }
    RoomBooking entity = mapper.toEntity(dto);
    entity.setRoom(room);
    entity.setBookedBy(user);
    RoomBooking saved = roomBookingRepo.save(entity);
    RoomBookingDTO result = mapper.toDTO(saved);
    notifyOnCreate(result);
    return result;
  }

  private void notifyOnCreate(RoomBookingDTO dto) {
    emailService.sendRoomBookingConfirmationEmail(dto);
    String content =
        String.format(
            "Room '%s' booked from %s to %s",
            dto.getRoomName(), dto.getStartTime(), dto.getEndTime());
    sendNotification(dto.getBookedById(), content, NotificationType.INFO);
  }

  @Override
  public Page<RoomBookingDTO> searchMyBookings(
      String roomName,
      LocalDateTime from,
      LocalDateTime to,
      BookingStatus status,
      int page,
      int size) {
    String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();
    Pageable pageable = PageRequest.of(page, size, Sort.by("bookingId").descending());
    Specification<RoomBooking> spec =
        specByUser(currentUser)
            .and(specByRoomName(roomName))
            .and(specByTimeRange(from, to))
            .and(specByStatus(status));
    return roomBookingRepo.findAll(spec, pageable).map(mapper::toDTO);
  }

  @Override
  public Page<RoomBookingDTO> searchBookings(
      String roomName,
      LocalDateTime from,
      LocalDateTime to,
      BookingStatus status,
      String userName,
      int page,
      int size) {
    Pageable pageable = PageRequest.of(page, size, Sort.by("bookingId").descending());
    Specification<RoomBooking> spec =
        specByRoomName(roomName)
            .and(specByTimeRange(from, to))
            .and(specByStatus(status))
            .and(specByBookedByName(userName));
    return roomBookingRepo.findAll(spec, pageable).map(mapper::toDTO);
  }

  // Specification helpers
  private Specification<RoomBooking> specByUser(String userName) {
    return (root, q, cb) -> cb.equal(root.get("bookedBy").get("userName"), userName);
  }

  private Specification<RoomBooking> specByRoomName(String roomName) {
    if (roomName == null || roomName.isBlank()) return Specification.where(null);
    return (root, q, cb) -> {
      Join<RoomBooking, Room> room = root.join("room");
      return cb.like(cb.lower(room.get("roomName")), "%" + roomName.toLowerCase() + "%");
    };
  }

  private Specification<RoomBooking> specByTimeRange(LocalDateTime from, LocalDateTime to) {
    Specification<RoomBooking> spec = Specification.where(null);
    if (from != null)
      spec = spec.and((r, q, cb) -> cb.greaterThanOrEqualTo(r.get("startTime"), from));
    if (to != null) spec = spec.and((r, q, cb) -> cb.lessThanOrEqualTo(r.get("endTime"), to));
    return spec;
  }

  private Specification<RoomBooking> specByStatus(BookingStatus status) {
    return status == null
        ? Specification.where(null)
        : (root, q, cb) -> cb.equal(root.get("status"), status);
  }

  private Specification<RoomBooking> specByBookedByName(String userName) {
    if (userName == null || userName.isBlank()) return Specification.where(null);
    return (root, q, cb) -> {
      Join<RoomBooking, User> user = root.join("bookedBy");
      return cb.like(cb.lower(user.get("userName")), "%" + userName.toLowerCase() + "%");
    };
  }

  @Override
  public List<RoomBookingDTO> getAll() {
    return toDTOList(roomBookingRepo.findAll());
  }

  @Override
  public List<RoomBookingDTO> getBookingsByUserName(String userName) {
    return toDTOList(roomBookingRepo.findByBookedBy_UserName(userName));
  }

  @Override
  public RoomBookingDTO update(Long id, RoomBookingDTO dto) {
    RoomBooking booking =
        roomBookingRepo.findById(id).orElseThrow(() -> new AppEx(ErrorCode.ROOM_BOOKING_NOT_FOUND));
    mapper.updateEntity(booking, dto);
    applyRoomAndUser(dto, booking);
    validateAndApplyTime(dto, id, booking);
    RoomBooking saved = roomBookingRepo.save(booking);
    notifyOnUpdate(saved);
    return mapper.toDTO(saved);
  }

  private void applyRoomAndUser(RoomBookingDTO dto, RoomBooking booking) {
    if (dto.getRoomId() != null) {
      Room room =
          roomRepo.findById(dto.getRoomId()).orElseThrow(() -> new AppEx(ErrorCode.ROOM_NOT_FOUND));
      booking.setRoom(room);
    }
    if (dto.getBookedById() != null) {
      User user =
          userRepo
              .findById(dto.getBookedById())
              .orElseThrow(() -> new AppEx(ErrorCode.USER_NOT_FOUND));
      booking.setBookedBy(user);
    }
  }

  private void validateAndApplyTime(RoomBookingDTO dto, Long id, RoomBooking booking) {
    if (dto.getStartTime() != null && dto.getEndTime() != null) {
      if (dto.getStartTime().isAfter(dto.getEndTime())) {
        throw new AppEx(ErrorCode.INVALID_TIME_RANGE);
      }
      boolean overlap =
          roomBookingRepo.existsByRoomAndTimeOverlapExcludingId(
              dto.getRoomId(), dto.getStartTime(), dto.getEndTime(), id);
      if (overlap) throw new AppEx(ErrorCode.ALREADY_BOOKED);
      booking.setStartTime(dto.getStartTime());
      booking.setEndTime(dto.getEndTime());
    }
  }

  private void notifyOnUpdate(RoomBooking booking) {
    String content =
        String.format(
            "Your booking for room '%s' has been updated to %s - %s",
            booking.getRoom().getRoomName(), booking.getStartTime(), booking.getEndTime());
    sendNotification(booking.getBookedBy().getUserId(), content, NotificationType.INFO);
  }

  @Override
  public void delete(Long id) {
    RoomBooking booking =
        roomBookingRepo.findById(id).orElseThrow(() -> new AppEx(ErrorCode.ROOM_BOOKING_NOT_FOUND));
    roomBookingRepo.deleteById(id);
  }

  @Override
  public ByteArrayOutputStream exportBookingsToExcel() throws IOException {
    try (Workbook wb = new XSSFWorkbook();
        ByteArrayOutputStream out = new ByteArrayOutputStream()) {
      createSheetWithData(wb, getAll());
      wb.write(out);
      return out;
    }
  }

  private void createSheetWithData(Workbook wb, List<RoomBookingDTO> bookings) {
    Sheet sheet = wb.createSheet("Bookings");
    String[] headers = {
      "Room ID", "Room Name", "Booked By", "Start Time", "End Time", "Status", "Description"
    };
    Font bold = wb.createFont();
    bold.setBold(true);
    CellStyle hdrStyle = wb.createCellStyle();
    hdrStyle.setFont(bold);
    Row hdr = sheet.createRow(0);
    for (int i = 0; i < headers.length; i++) {
      Cell c = hdr.createCell(i);
      c.setCellValue(headers[i]);
      c.setCellStyle(hdrStyle);
      sheet.autoSizeColumn(i);
    }
    int r = 1;
    for (RoomBookingDTO dto : bookings) {
      Row row = sheet.createRow(r++);
      row.createCell(0).setCellValue(dto.getRoomId());
      row.createCell(1).setCellValue(dto.getRoomName());
      row.createCell(2).setCellValue(dto.getUserName());
      row.createCell(3).setCellValue(dto.getStartTime().toString());
      row.createCell(4).setCellValue(dto.getEndTime().toString());
      row.createCell(5).setCellValue(dto.getStatus().name());
      row.createCell(6).setCellValue(dto.getDescription());
    }
  }

  @Override
  @Scheduled(fixedRate = REMINDER_INTERVAL_MS)
  @Transactional
  public void sendMeetingReminderEmails() {
    LocalDateTime now = LocalDateTime.now();
    LocalDateTime nextHour = now.plusHours(1);
    roomBookingRepo.findMeetingsBetween(now, nextHour).forEach(this::sendReminderEmailSafely);
  }

  private void sendReminderEmailSafely(RoomBooking booking) {
    try {
      emailService.sendMeetingReminderEmail(
          booking.getBookedBy().getEmail(),
          booking.getBookedBy().getUserName(),
          booking.getRoom().getRoomName(),
          booking.getStartTime().toString(),
          booking.getEndTime().toString());
    } catch (MessagingException e) {
      log.error("Failed to send reminder for booking {}", booking.getBookingId(), e);
    }
  }

  @Override
  public List<RoomBookingDTO> getBookingsByRoomId(Long roomId) {
    Room room = roomRepo.findById(roomId)
            .orElseThrow(() -> new AppEx(ErrorCode.ROOM_NOT_FOUND));
    return toDTOList(
            room.getBookings().stream()
                    .filter(b -> b.getStatus() == BookingStatus.CONFIRMED)
                    .toList()
    );
  }

  @Override
  public List<RoomBookingDTO> getUpcomingBookings() {
    return toDTOList(
            roomBookingRepo.findByStatusAndStartTimeAfter(
                    BookingStatus.CONFIRMED, LocalDateTime.now()));
  }

  @Override
  public List<RoomBookingDTO> getUpcomingBookingsByUserName(String userName) {
    return toDTOList(
            roomBookingRepo.findByBookedBy_UserNameAndStatusAndStartTimeAfter(
                    userName, BookingStatus.CONFIRMED, LocalDateTime.now()));
  }

  @Override
  public List<RoomBookingDTO> getUpcomingBookingsByRoomId(Long roomId) {
    return toDTOList(
            roomBookingRepo.findByRoom_roomIdAndStatusAndStartTimeAfter(
                    roomId, BookingStatus.CONFIRMED, LocalDateTime.now()));
  }

  @Override
  public Page<RoomBookingDTO> getMyUpcomingBookings(
      String roomName, LocalDateTime from, LocalDateTime to, int page, int size) {
    String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();
    LocalDateTime now = LocalDateTime.now();
    Pageable pageable = PageRequest.of(page, size, Sort.by("bookingId").descending());
    Specification<RoomBooking> spec =
        specByUser(currentUser)
            .and((r, q, cb) -> cb.equal(r.get("status"), BookingStatus.CONFIRMED))
            .and((r, q, cb) -> cb.greaterThan(r.get("startTime"), now))
            .and(specByRoomName(roomName))
            .and(specByTimeRange(from, to));
    return roomBookingRepo.findAll(spec, pageable).map(mapper::toDTO);
  }

  @Override
  @Transactional
  public RoomBookingDTO cancelBooking(Long id) {
    RoomBooking booking =
        roomBookingRepo.findById(id).orElseThrow(() -> new AppEx(ErrorCode.ROOM_BOOKING_NOT_FOUND));
    if (booking.getStatus() != BookingStatus.CANCELLED) {
      booking.setStatus(BookingStatus.CANCELLED);
      roomBookingRepo.save(booking);
      String content =
          String.format(
              "Your booking for room '%s' from %s to %s has been cancelled.",
              booking.getRoom().getRoomName(), booking.getStartTime(), booking.getEndTime());
      sendNotification(booking.getBookedBy().getUserId(), content, NotificationType.WARNING);
    }
    return mapper.toDTO(booking);
  }

  @Override
  @Transactional
  public void cancelMultipleBookings(List<Long> ids) {
    if (ids == null || ids.isEmpty()) {
      throw new IllegalArgumentException("Booking IDs cannot be empty");
    }
    List<RoomBooking> bookings = roomBookingRepo.findAllById(ids);
    if (bookings.size() != ids.size()) {
      throw new AppEx(ErrorCode.BATCH_CANCELLATION_FAILED);
    }
    bookings.forEach(b -> b.setStatus(BookingStatus.CANCELLED));
    roomBookingRepo.saveAll(bookings);
    bookings.forEach(
        b -> {
          String content =
              String.format(
                  "Your booking for room '%s' from %s to %s has been cancelled.",
                  b.getRoom().getRoomName(), b.getStartTime(), b.getEndTime());
          sendNotification(b.getBookedBy().getUserId(), content, NotificationType.WARNING);
        });
  }

  private void sendNotification(Long userId, String content, NotificationType type) {
    NotificationDTO dto =
        NotificationDTO.builder().userId(userId).content(content).type(type).build();
    notificationService.create(dto);
  }

  private List<RoomBookingDTO> toDTOList(List<RoomBooking> bookings) {
    return bookings.stream()
            .map(mapper::toDTO)
            .collect(Collectors.toList());
  }

}
