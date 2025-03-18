package org.training.meetingroombooking.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.training.meetingroombooking.entity.dto.RoomBookingDTO;
import org.training.meetingroombooking.entity.enums.ErrorCode;
import org.training.meetingroombooking.entity.mapper.RoomBookingMapper;
import org.training.meetingroombooking.entity.models.RoomBooking;
import org.training.meetingroombooking.exception.AppEx;
import org.training.meetingroombooking.repository.RoomBookingRepository;

@Service
public class RoomBookingService {

  private final RoomBookingRepository roomBookingRepository;
  private final RoomBookingMapper roomBookingMapper;

  public RoomBookingService(RoomBookingRepository roomBookingRepository, RoomBookingMapper roomBookingMapper) {
    this.roomBookingRepository = roomBookingRepository;
    this.roomBookingMapper = roomBookingMapper;
  }

  public RoomBookingDTO create(RoomBookingDTO dto) {
    RoomBooking roomBooking = roomBookingMapper.toEntity(dto);
    RoomBooking savedRoomBooking = roomBookingRepository.save(roomBooking);
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
    throw new AppEx(ErrorCode.ROOMBOOKING_NOT_FOUND);
  }
  public void delete(Long bookingId) {
    if (roomBookingRepository.existsById(bookingId)) {
      throw new AppEx(ErrorCode.ROOMBOOKING_NOT_FOUND);
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
}
