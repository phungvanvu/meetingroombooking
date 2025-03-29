package org.training.meetingroombooking.service;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.training.meetingroombooking.entity.dto.RoomBookingDTO;
import org.training.meetingroombooking.entity.dto.RoomDTO;
import org.training.meetingroombooking.entity.dto.Summary.RoomStatisticsDTO;
import org.training.meetingroombooking.entity.enums.ErrorCode;
import org.training.meetingroombooking.entity.mapper.RoomMapper;
import org.training.meetingroombooking.entity.models.Room;
import org.training.meetingroombooking.exception.AppEx;
import org.training.meetingroombooking.repository.RoomRepository;

@Service
public class RoomService {

  private final RoomRepository roomRepository;
  private final RoomMapper roomMapper;

  public RoomService(RoomRepository roomRepository, RoomMapper roomMapper) {
    this.roomRepository = roomRepository;
    this.roomMapper = roomMapper;
  }

  public RoomDTO create(RoomDTO dto, MultipartFile file) throws IOException {
    Room room = roomMapper.toEntity(dto);
    // Nếu có ảnh, lưu vào thư mục
    if (file != null && !file.isEmpty()) {
      String originalFilename = file.getOriginalFilename();
      if (originalFilename == null || !originalFilename.matches(".*\\.(png|jpg|jpeg)$")) {
        throw new AppEx(ErrorCode.INVALID_FILE_TYPE);
      }
      String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
      String fileName = dto.getRoomName().replaceAll("[^a-zA-Z0-9.-]", "_") + fileExtension;
      String uploadDir = "uploads/rooms/";
      File uploadPath = new File(uploadDir);
      if (!uploadPath.exists()) {
        uploadPath.mkdirs();
      }
      Path filePath = Paths.get(uploadDir, fileName);
      Files.write(filePath, file.getBytes());
      room.setImageUrl("/uploads/rooms/" + fileName);
    }
    Room savedRoom = roomRepository.save(room);
    return roomMapper.toDTO(savedRoom);
  }

  public RoomDTO findById(Long roomId) {
    Optional<Room> room = roomRepository.findById(roomId);
    return room.map(roomMapper::toDTO).orElseThrow(
        () -> new AppEx(ErrorCode.ROOM_NOT_FOUND));
  }

  public List<RoomDTO> getAll() {
    List<Room> rooms = roomRepository.findAll();
    return rooms.stream()
        .map(roomMapper::toDTO)
        .collect(Collectors.toList());
  }

  public ByteArrayOutputStream exportRoomsToExcel() throws IOException {
    List<RoomDTO> rooms = getAll();

    try (Workbook workbook = new XSSFWorkbook();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
      Sheet sheet = workbook.createSheet("Rooms");
      // Tạo hàng tiêu đề
      Row headerRow = sheet.createRow(0);
      String[] headers = {"Room ID", "Room Name", "Location",
          "Capacity", "Note", "isAvailable", "ImageUrl"};
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
      for (RoomDTO room : rooms) {
        Row row = sheet.createRow(rowNum++);
        if (room.getRoomId() != null) {
          row.createCell(0).setCellValue(room.getRoomId());
        } else {
          row.createCell(0).setCellValue("N/A");
        }
        row.createCell(1).setCellValue(room.getRoomName() != null ? room.getRoomName() : "N/A");
        row.createCell(2).setCellValue(room.getLocation() != null ? room.getLocation() : "N/A");
        row.createCell(3).setCellValue(
            room.getCapacity() != null ? room.getCapacity().toString() : "N/A");
        row.createCell(4).setCellValue(
            room.getNote() != null ? room.getNote() : "N/A");
        row.createCell(5).setCellValue(room.isAvailable());
        row.createCell(6).setCellValue(room.getImageUrl() != null ? room.getImageUrl() : "N/A");
      }
      for (int i = 0; i < headers.length; i++) {
        sheet.autoSizeColumn(i);
      }
      workbook.write(outputStream);
      return outputStream;
    }
  }

  public RoomDTO update(Long roomId, RoomDTO dto, MultipartFile file) throws IOException {
    Room room = roomRepository.findById(roomId)
        .orElseThrow(() -> new AppEx(ErrorCode.ROOM_NOT_FOUND));
    roomMapper.updateRoom(room, dto);
    // Nếu có ảnh mới, lưu lại
    if (file != null && !file.isEmpty()) {
      String originalFilename = file.getOriginalFilename();
      if (originalFilename == null || !originalFilename.matches(".*\\.(png|jpg|jpeg)$")) {
        throw new AppEx(ErrorCode.INVALID_FILE_TYPE);
      }
      String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
      String fileName = dto.getRoomName().replaceAll("[^a-zA-Z0-9.-]", "_") + fileExtension;
      String uploadDir = "uploads/rooms/";
      File uploadPath = new File(uploadDir);
      if (!uploadPath.exists()) {
        uploadPath.mkdirs();
      }
      Path filePath = Paths.get(uploadDir, fileName);
      Files.write(filePath, file.getBytes());
      room.setImageUrl("/uploads/rooms/" + fileName);
    }
    Room updatedRoom = roomRepository.save(room);
    return roomMapper.toDTO(updatedRoom);
  }

  public void delete(Long roomId) {
    if (!roomRepository.existsById(roomId)) {
      throw new AppEx(ErrorCode.ROOM_NOT_FOUND);
    }
    roomRepository.deleteById(roomId);
  }

  public RoomDTO findByRoomName(String roomName) {
    Room room = roomRepository.findByRoomName(roomName)
        .orElseThrow(() -> new AppEx(ErrorCode.ROOM_NOT_FOUND));
    return roomMapper.toDTO(room);
  }

  public Set<String> getAllRoomNames() {
    return roomRepository.findAll()
            .stream()
            .map(Room::getRoomName)
            .collect(Collectors.toSet());
  }
  public Set<String> getAllRoomLocations() {
    return roomRepository.findAll()
            .stream()
            .map(Room::getLocation)
            .collect(Collectors.toSet());
  }
}
