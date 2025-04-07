package org.training.meetingroombooking.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import jakarta.persistence.criteria.Join;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.training.meetingroombooking.entity.dto.RoomBookingDTO;
import org.training.meetingroombooking.entity.enums.BookingStatus;
import org.training.meetingroombooking.entity.enums.ErrorCode;
import org.training.meetingroombooking.entity.mapper.RoomBookingMapper;
import org.training.meetingroombooking.entity.models.Room;
import org.training.meetingroombooking.entity.models.RoomBooking;
import org.training.meetingroombooking.exception.AppEx;
import org.training.meetingroombooking.repository.RoomBookingRepository;
import org.training.meetingroombooking.repository.RoomRepository;
import org.training.meetingroombooking.repository.UserRepository;
import org.training.meetingroombooking.entity.models.User;

@Service
public class RoomBookingService {

    private final RoomBookingRepository roomBookingRepository;
    private final RoomBookingMapper roomBookingMapper;
    private final EmailService emailService;
    private final RoomRepository roomRepository;
    private final UserRepository userRepository;

    public RoomBookingService(RoomBookingRepository roomBookingRepository,
                              RoomBookingMapper roomBookingMapper, EmailService emailService,
                              RoomRepository roomRepository, UserRepository userRepository) {
        this.roomBookingRepository = roomBookingRepository;
        this.roomBookingMapper = roomBookingMapper;
        this.emailService = emailService;
        this.roomRepository = roomRepository;
        this.userRepository = userRepository;
    }

    public RoomBookingDTO create(RoomBookingDTO dto) {
        Room room = roomRepository.findById(dto.getRoomId())
                .orElseThrow(() -> new AppEx(ErrorCode.ROOM_NOT_FOUND));
        User user = userRepository.findById(dto.getBookedById())
                .orElseThrow(() -> new AppEx(ErrorCode.USER_NOT_FOUND));
        boolean isOverlap = roomBookingRepository.existsByRoomAndTimeOverlap(
                dto.getRoomId(), dto.getStartTime(), dto.getEndTime());
        if (isOverlap) {
            throw new AppEx(ErrorCode.ALREADY_BOOKED);
        }
        RoomBooking roomBooking = roomBookingMapper.toEntity(dto);
        roomBooking.setRoom(room);
        roomBooking.setBookedBy(user);
        RoomBooking savedRoomBooking = roomBookingRepository.save(roomBooking);
        RoomBookingDTO savedDTO = roomBookingMapper.toDTO(savedRoomBooking);
        emailService.sendRoomBookingConfirmationEmail(savedDTO);
        return savedDTO;
    }

    /**
     * Lấy danh sách RoomBooking của người dùng hiện hành theo các tiêu chí:
     * - roomName: tìm kiếm theo tên phòng (không phân biệt chữ hoa chữ thường)
     * - fromTime: thời gian bắt đầu (startTime) từ
     * - toTime: thời gian kết thúc (endTime) đến
     * - status: trạng thái đặt phòng
     * - Phân trang theo thứ tự giảm dần của bookingId
     */
    public Page<RoomBookingDTO> searchMyBookings(String roomName, LocalDateTime fromTime, LocalDateTime toTime, BookingStatus status, int page, int size) {
        String currentUserName = SecurityContextHolder.getContext().getAuthentication().getName();
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "bookingId"));
        Specification<RoomBooking> spec = Specification.where(
                (***REMOVED***, query, cb) -> cb.equal(***REMOVED***.get("bookedBy").get("userName"), currentUserName)
        );
        if (roomName != null && !roomName.isEmpty()) {
            spec = spec.and((***REMOVED***, query, cb) -> {
                Join<RoomBooking, Room> roomJoin = ***REMOVED***.join("room");
                return cb.like(cb.lower(roomJoin.get("roomName")), "%" + roomName.toLowerCase() + "%");
            });
        }
        if (fromTime != null) {
            spec = spec.and((***REMOVED***, query, cb) -> cb.greaterThanOrEqualTo(***REMOVED***.get("startTime"), fromTime));
        }
        if (toTime != null) {
            spec = spec.and((***REMOVED***, query, cb) -> cb.lessThanOrEqualTo(***REMOVED***.get("endTime"), toTime));
        }
        if (status != null) {
            spec = spec.and((***REMOVED***, query, cb) -> cb.equal(***REMOVED***.get("status"), status));
        }
        Page<RoomBooking> bookingPage = roomBookingRepository.findAll(spec, pageable);
        return bookingPage.map(roomBookingMapper::toDTO);
    }

    public List<RoomBookingDTO> getAll() {
        List<RoomBooking> roomBookings = roomBookingRepository.findAll();
        return roomBookings.stream()
                .map(roomBookingMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<RoomBookingDTO> getBookingsByUserName(String userName) {
        List<RoomBooking> roomBookings = roomBookingRepository.findByBookedBy_UserName(userName);
        return roomBookings.stream()
                .map(roomBookingMapper::toDTO)
                .collect(Collectors.toList());
    }

    public RoomBookingDTO update(Long bookingId, RoomBookingDTO dto) {
        RoomBooking roomBooking = roomBookingRepository.findById(bookingId)
                .orElseThrow(() -> new AppEx(ErrorCode.ROOM_BOOKING_NOT_FOUND));
        roomBookingMapper.updateEntity(roomBooking, dto);
        if (dto.getRoomId() != null) {
            Room room = roomRepository.findById(dto.getRoomId())
                    .orElseThrow(() -> new AppEx(ErrorCode.ROOM_NOT_FOUND));
            roomBooking.setRoom(room);
        }
        if (dto.getBookedById() != null) {
            User user = userRepository.findById(dto.getBookedById())
                    .orElseThrow(() -> new AppEx(ErrorCode.USER_NOT_FOUND));
            roomBooking.setBookedBy(user);
        }
        if (dto.getStartTime() != null && dto.getEndTime() != null) {
            boolean isOverlap = roomBookingRepository.existsByRoomAndTimeOverlapExcludingId(
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
        return roomBookingMapper.toDTO(updatedRoomBooking);
    }

    public void delete(Long bookingId) {
        if (!roomBookingRepository.existsById(bookingId)) {
            throw new AppEx(ErrorCode.ROOM_BOOKING_NOT_FOUND);
        }
        roomBookingRepository.deleteById(bookingId);
    }

    public ByteArrayOutputStream exportBookingsToExcel() throws IOException {
        List<RoomBookingDTO> bookings = getAll();
        try (Workbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Bookings");
            // Tạo hàng tiêu đề
            Row headerRow = sheet.createRow(0);
            String[] headers = {"Room ID", "Room Name", "Booked By",
                    "Start Time", "End Time", "Status", "Description"};
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
                row.createCell(1).setCellValue(booking.getRoomName() != null ? booking.getRoomName() : "N/A");
                row.createCell(2).setCellValue(booking.getUserName() != null ? booking.getUserName() : "N/A");
                row.createCell(3).setCellValue(
                        booking.getStartTime() != null ? booking.getStartTime().toString() : "N/A");
                row.createCell(4).setCellValue(
                        booking.getEndTime() != null ? booking.getEndTime().toString() : "N/A");
                row.createCell(5).setCellValue(
                        booking.getStatus() != null ? booking.getStatus().toString() : "N/A");
                row.createCell(6).setCellValue(booking.getDescription() != null ? booking.getDescription() : "N/A");
            }
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }
            workbook.write(outputStream);
            return outputStream;
        }
    }

//    @Scheduled(fixedRate = 3600000)//1h
//    @Transactional
//    public void sendMeetingReminderEmails() {
//        LocalDateTime now = LocalDateTime.now();
//        LocalDateTime HoursLater = now.plus(1, ChronoUnit.HOURS);
//        List<RoomBooking> upcomingMeetings = roomBookingRepository
//            .findMeetingsBetween(now, HoursLater);
//        for (RoomBooking booking : upcomingMeetings) {
//            try {
//                emailService.sendMeetingReminderEmail(
//                        booking.getBookedBy().getEmail(),
//                        booking.getBookedBy().getUserName(),
//                        booking.getRoom().getRoomName(),
//                        booking.getStartTime().toString(),
//                        booking.getEndTime().toString()
//                );
//            } catch (MessagingException e) {
//                e.printStackTrace();
//            }
//        }
//    }

    public List<RoomBookingDTO> getBookingsByRoomName(String roomName) {
        Room room = roomRepository.findByRoomName(roomName)
                .orElseThrow(() -> new AppEx(ErrorCode.ROOM_NOT_FOUND));
        List<RoomBooking> roomBookings = room.getBookings();
        if (roomBookings.isEmpty()) {
            return Collections.emptyList();
        }
        return roomBookings.stream()
                .map(roomBookingMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<RoomBookingDTO> getBookingsByRoomId(Long roomId) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new AppEx(ErrorCode.ROOM_NOT_FOUND));
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
        List<RoomBooking> upcomingBookings = roomBookingRepository
                .findByStatusAndStartTimeAfter(BookingStatus.CONFIRMED, now);
        return upcomingBookings.stream()
                .map(roomBookingMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<RoomBookingDTO> getUpcomingBookingsByUserName(String userName) {
        LocalDateTime now = LocalDateTime.now();
        List<RoomBooking> upcomingBookings = roomBookingRepository
                .findByBookedBy_UserNameAndStatusAndStartTimeAfter(
                        userName, BookingStatus.CONFIRMED, now);
        return upcomingBookings.stream()
                .map(roomBookingMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<RoomBookingDTO> getUpcomingBookingsByRoomId(Long roomId) {
        LocalDateTime now = LocalDateTime.now();
        List<RoomBooking> upcomingBookings = roomBookingRepository
                .findByRoom_roomIdAndStatusAndStartTimeAfter(
                        roomId, BookingStatus.CONFIRMED, now);
        return upcomingBookings.stream()
                .map(roomBookingMapper::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Lấy danh sách các booking sắp tới (startTime > hiện tại) của người dùng hiện tại, có:
     * - roomName: tìm kiếm tên phòng (không phân biệt chữ hoa thường)
     * - fromTime: thời gian bắt đầu từ
     * - toTime: thời gian kết thúc đến
     * - Phân trang theo thứ tự giảm dần bookingId
     */
    public Page<RoomBookingDTO> getMyUpcomingBookings(String roomName, LocalDateTime fromTime, LocalDateTime toTime, int page, int size) {
        String currentUserName = SecurityContextHolder.getContext().getAuthentication().getName();
        LocalDateTime now = LocalDateTime.now();
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "bookingId"));
        Specification<RoomBooking> spec = Specification.where(
                (***REMOVED***, query, cb) -> cb.and(
                        cb.equal(***REMOVED***.get("bookedBy").get("userName"), currentUserName),
                        cb.equal(***REMOVED***.get("status"), BookingStatus.CONFIRMED),
                        cb.greaterThan(***REMOVED***.get("startTime"), now)
                )
        );
        if (roomName != null && !roomName.isEmpty()) {
            spec = spec.and((***REMOVED***, query, cb) -> {
                Join<RoomBooking, Room> roomJoin = ***REMOVED***.join("room");
                return cb.like(cb.lower(roomJoin.get("roomName")), "%" + roomName.toLowerCase() + "%");
            });
        }
        if (fromTime != null) {
            spec = spec.and((***REMOVED***, query, cb) -> cb.greaterThanOrEqualTo(***REMOVED***.get("startTime"), fromTime));
        }
        if (toTime != null) {
            spec = spec.and((***REMOVED***, query, cb) -> cb.lessThanOrEqualTo(***REMOVED***.get("endTime"), toTime));
        }
        Page<RoomBooking> resultPage = roomBookingRepository.findAll(spec, pageable);
        return resultPage.map(roomBookingMapper::toDTO);
    }


    public RoomBookingDTO cancelBooking(Long bookingId) {
        RoomBooking roomBooking = roomBookingRepository.findById(bookingId)
                .orElseThrow(() -> new AppEx(ErrorCode.ROOM_BOOKING_NOT_FOUND));
        if (roomBooking.getStatus() == BookingStatus.CANCELLED) {
            return roomBookingMapper.toDTO(roomBooking);
        }
        roomBooking.setStatus(BookingStatus.CANCELLED);
        RoomBooking cancelledBooking = roomBookingRepository.save(roomBooking);
        return roomBookingMapper.toDTO(cancelledBooking);
    }

}
