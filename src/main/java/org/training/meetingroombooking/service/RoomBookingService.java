package org.training.meetingroombooking.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import jakarta.mail.MessagingException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.training.meetingroombooking.entity.dto.RoomBookingDTO;
import org.training.meetingroombooking.entity.dto.RoomSummaryDTO;
import org.training.meetingroombooking.entity.enums.ErrorCode;
import org.training.meetingroombooking.entity.mapper.RoomBookingMapper;
import org.training.meetingroombooking.entity.models.Room;
import org.training.meetingroombooking.entity.models.RoomBooking;
import org.training.meetingroombooking.exception.AppEx;
import org.training.meetingroombooking.repository.RoomBookingRepository;
import org.training.meetingroombooking.repository.RoomRepository;

@Service
public class RoomBookingService {

  private final RoomBookingRepository roomBookingRepository;
  private final RoomBookingMapper roomBookingMapper;
  private final EmailService emailService;
  private final RoomRepository roomRepository;

  public RoomBookingService(RoomBookingRepository roomBookingRepository,
                            RoomBookingMapper roomBookingMapper,
                            EmailService emailService,
                            RoomRepository roomRepository) {
    this.roomBookingRepository = roomBookingRepository;
    this.roomBookingMapper = roomBookingMapper;
    this.emailService = emailService;
    this.roomRepository = roomRepository;
  }

  public RoomSummaryDTO getMostBookedRoomOfMonth() {
    LocalDate currentDate = LocalDate.now();
    int month = currentDate.getMonthValue();
    int year = currentDate.getYear();

    Optional<Object[]> result = roomBookingRepository.findMostBookedRoomOfMonth(month, year);

    if (result.isPresent()) {
      Long roomId = (Long) result.get()[0];
      long count = (Long) result.get()[1];

      return new RoomSummaryDTO(roomId, "Most booked room of the month", count);
    }
    throw new AppEx(ErrorCode.ROOM_BOOKING_NOT_FOUND);
  }

  public long getMonthlyBookingCount(int month, int year) {
    return roomBookingRepository.countBookingsByMonth(month, year);
  }

  public long getCurrentMonthBookingCount() {
    LocalDate currentDate = LocalDate.now();
    return getMonthlyBookingCount(currentDate.getMonthValue(), currentDate.getYear());
  }

  public RoomBookingDTO create(RoomBookingDTO dto) {
    RoomBooking roomBooking = roomBookingMapper.toEntity(dto);
    RoomBooking savedRoomBooking = roomBookingRepository.save(roomBooking);
    // Gửi email sau khi tạo thành công
    try {
      emailService.sendRoomBookingConfirmationEmail(dto);
    } catch (MessagingException e) {
      e.printStackTrace();
    }
    return roomBookingMapper.toDTO(savedRoomBooking);
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

  public List<RoomBookingDTO> getMyBookings() {
    String userName = SecurityContextHolder.getContext().getAuthentication().getName();
    return getBookingsByUserName(userName);
  }

  public RoomBookingDTO update(Long bookingId, RoomBookingDTO dto) {
    Optional<RoomBooking> existingRoomBooking = roomBookingRepository.findById(bookingId);
    if (existingRoomBooking.isPresent()) {
      RoomBooking roomBooking = existingRoomBooking.get();
      roomBookingMapper.updateEntity(roomBooking, dto);
      RoomBooking updatedRoomBooking = roomBookingRepository.save(roomBooking);
      return roomBookingMapper.toDTO(updatedRoomBooking);
    }
    throw new AppEx(ErrorCode.ROOM_BOOKING_NOT_FOUND);
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
      String[] headers = {"Room ID", "Room Name", "Booked By", "Start Time", "End Time", "Status", "Note"};

      for (int i = 0; i < headers.length; i++) {
        Cell cell = headerRow.createCell(i);
        cell.setCellValue(headers[i]);
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);
        cell.setCellStyle(style);
      }

      // Điền dữ liệu vào sheet
      int rowNum = 1;
      for (RoomBookingDTO booking : bookings) {
        Row row = sheet.createRow(rowNum++);
        row.createCell(0).setCellValue(booking.getRoomId());
        row.createCell(1).setCellValue(booking.getRoomName() != null ? booking.getRoomName() : "N/A");
        row.createCell(2).setCellValue(booking.getUserName() != null ? booking.getUserName() : "N/A");
        row.createCell(3).setCellValue(booking.getStartTime().toString());
        row.createCell(4).setCellValue(booking.getEndTime().toString());
        row.createCell(5).setCellValue(booking.getStatus().toString());
        row.createCell(6).setCellValue(booking.getNote() != null ? booking.getNote() : "");
      }
      // Tự động điều chỉnh độ rộng của cột
      for (int i = 0; i < headers.length; i++) {
        sheet.autoSizeColumn(i);
      }
      workbook.write(outputStream);
      return outputStream;
    }
  }

  private CellStyle getHeaderCellStyle(Workbook workbook) {
    CellStyle style = workbook.createCellStyle();
    Font font = workbook.createFont();
    font.setBold(true);
    style.setFont(font);
    return style;
  }

  @Scheduled(fixedRate = 900000) // tự động thực hiện mỗi 15 phút
  @Transactional
  public void sendMeetingReminderEmails() {
    LocalDateTime now = LocalDateTime.now();
    LocalDateTime twoHoursLater = now.plus(2, ChronoUnit.HOURS);

    List<RoomBooking> upcomingMeetings = roomBookingRepository.findMeetingsBetween(now, twoHoursLater);

    for (RoomBooking booking : upcomingMeetings) {
      try {
        emailService.sendMeetingReminderEmail(
                booking.getBookedBy().getEmail(),
                booking.getBookedBy().getUserName(),
                booking.getRoom().getRoomName(),
                booking.getStartTime().toString(),
                booking.getEndTime().toString()
        );
      } catch (MessagingException e) {
        e.printStackTrace();
      }
    }
  }

  public List<RoomBookingDTO> getBookingsByRoomName(String roomName) {
    Room room = roomRepository.findByRoomName(roomName)
            .orElseThrow(() -> new AppEx(ErrorCode.ROOM_NOT_FOUND));

    List<RoomBooking> roomBookings = room.getBookings();
    if (roomBookings.isEmpty()) {
      throw new AppEx(ErrorCode.ROOM_BOOKING_NOT_FOUND);
    }

    return roomBookings.stream()
            .map(roomBookingMapper::toDTO)
            .collect(Collectors.toList());
  }
}
