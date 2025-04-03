package org.training.meetingroombooking.service;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import jakarta.persistence.criteria.Join;
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
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.training.meetingroombooking.entity.dto.RoomDTO;
import org.training.meetingroombooking.entity.enums.ErrorCode;
import org.training.meetingroombooking.entity.mapper.RoomMapper;
import org.training.meetingroombooking.entity.models.Equipment;
import org.training.meetingroombooking.entity.models.Room;
import org.training.meetingroombooking.exception.AppEx;
import org.training.meetingroombooking.repository.EquipmentRepository;
import org.training.meetingroombooking.repository.RoomRepository;

@Service
public class RoomService {

  private final RoomRepository roomRepository;
  private final RoomMapper roomMapper;
  private final EquipmentRepository equipmentRepository;
  private final S3Service s3Service;

  public RoomService(RoomRepository roomRepository, RoomMapper roomMapper,
                     EquipmentRepository equipmentRepository, S3Service s3Service) {
    this.roomRepository = roomRepository;
    this.roomMapper = roomMapper;
    this.equipmentRepository = equipmentRepository;
    this.s3Service = s3Service;
  }

  public RoomDTO create(RoomDTO dto, MultipartFile file) throws IOException {
    Room room = roomMapper.toEntity(dto);
    Set<Equipment> equipmentSet = new HashSet<>();
    for (String equipmentName : dto.getEquipments()) {
      Equipment equipment = equipmentRepository.findById(equipmentName).orElse(null);
      if (equipment == null) {
        throw new AppEx(ErrorCode.EQUIPMENT_NOT_FOUND);
      }
      equipmentSet.add(equipment);
    }
    room.setEquipments(equipmentSet);
    // Nếu có file, upload file lên S3
    if (file != null && !file.isEmpty()) {
      String originalFilename = file.getOriginalFilename();
      if (originalFilename == null || !originalFilename.matches(".*\\.(png|jpg|jpeg)$")) {
        throw new AppEx(ErrorCode.INVALID_FILE_TYPE);
      }
      String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
      String fileName = dto.getRoomName().replaceAll("[^a-zA-Z0-9.-]", "_") + fileExtension;
      String s3Key = "rooms/" + fileName;
      // Upload file lên S3 và nhận lại URL
      String fileUrl = s3Service.uploadFile(s3Key, file);
      room.setImageUrl(fileUrl);
    }
    // Lưu phòng vào cơ sở dữ liệu
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

  /**
   * Lấy danh sách phòng với phân trang và lọc theo:
   * - roomName: tìm kiếm theo tên (không phân biệt chữ hoa thường)
   * - locations: danh sách địa điểm
   * - available: trạng thái (Available hay Unavailable)
   * - capacities: danh sách sức chứa
   * - equipments: tập hợp tên thiết bị
   * Sắp xếp theo thứ tự giảm dần (mới nhất hiển thị đầu tiên)
   */
  public Page<RoomDTO> getRooms(String roomName, List<String> locations, Boolean available,
                                List<Integer> capacities, Set<String> equipments, int page, int size) {
    Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "roomId"));
    Specification<Room> spec = Specification.where(null);

    if (roomName != null && !roomName.isEmpty()) {
      spec = spec.and((***REMOVED***, query, cb) ->
              cb.like(cb.lower(***REMOVED***.get("roomName")), "%" + roomName.toLowerCase() + "%")
      );
    }
    if (locations != null && !locations.isEmpty()) {
      spec = spec.and((***REMOVED***, query, cb) ->
              ***REMOVED***.get("location").in(locations)
      );
    }
    if (available != null) {
      spec = spec.and((***REMOVED***, query, cb) ->
              cb.equal(***REMOVED***.get("available"), available)
      );
    }
    if (capacities != null && !capacities.isEmpty()) {
      spec = spec.and((***REMOVED***, query, cb) ->
              ***REMOVED***.get("capacity").in(capacities)
      );
    }
    if (equipments != null && !equipments.isEmpty()) {
      spec = spec.and((***REMOVED***, query, cb) -> {
        Join<Room, Equipment> equipmentJoin = ***REMOVED***.join("equipments");
        return equipmentJoin.get("equipmentName").in(equipments);
      });
      spec = spec.and((***REMOVED***, query, cb) -> {
        query.distinct(true);
        return cb.conjunction();
      });
    }
    Page<Room> roomsPage = roomRepository.findAll(spec, pageable);
    return roomsPage.map(roomMapper::toDTO);
  }

  public ByteArrayOutputStream exportRoomsToExcel() throws IOException {
    List<RoomDTO> rooms = getAll();
    Workbook workbook = null;
    InputStream templateStream = getClass().getResourceAsStream("/templates/rooms_template.xlsx");
    if (templateStream != null) {
      workbook = new XSSFWorkbook(templateStream);
    } else {
      workbook = new XSSFWorkbook();
    }
    Sheet sheet = workbook.getNumberOfSheets() > 0 ? workbook.getSheetAt(0) : workbook.createSheet("Rooms");
    if (sheet.getPhysicalNumberOfRows() == 0) {
      Row headerRow = sheet.createRow(0);
      String[] headers = {"Room Name", "Location", "Capacity", "Note", "isAvailable", "ImageUrl"};
      for (int i = 0; i < headers.length; i++) {
        Cell cell = headerRow.createCell(i);
        cell.setCellValue(headers[i]);
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);
        cell.setCellStyle(style);
      }
    }
    int rowNum = sheet.getLastRowNum() + 1;
    for (RoomDTO room : rooms) {
      Row row = sheet.createRow(rowNum++);
      row.createCell(1).setCellValue(room.getRoomName() != null ? room.getRoomName() : "N/A");
      row.createCell(2).setCellValue(room.getLocation() != null ? room.getLocation() : "N/A");
      row.createCell(3).setCellValue(room.getCapacity() != null ? room.getCapacity().toString() : "N/A");
      row.createCell(4).setCellValue(room.getNote() != null ? room.getNote() : "N/A");
      row.createCell(5).setCellValue(room.isAvailable());
      row.createCell(6).setCellValue(room.getImageUrl() != null ? room.getImageUrl() : "N/A");
    }
    int headerCount = 7;
    for (int i = 0; i < headerCount; i++) {
      sheet.autoSizeColumn(i);
    }
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    workbook.write(outputStream);
    workbook.close();
    return outputStream;
  }

  public RoomDTO update(Long roomId, RoomDTO dto, MultipartFile file) throws IOException {
    Room room = roomRepository.findById(roomId)
            .orElseThrow(() -> new AppEx(ErrorCode.ROOM_NOT_FOUND));
    Set<Equipment> equipmentSet = new HashSet<>();
    for (String equipmentName : dto.getEquipments()) {
      Equipment equipment = equipmentRepository.findById(equipmentName).orElse(null);
      if (equipment == null) {
        throw new AppEx(ErrorCode.EQUIPMENT_NOT_FOUND);
      }
      equipmentSet.add(equipment);
    }
    room.setEquipments(equipmentSet);
    roomMapper.updateRoom(room, dto);
    if (file != null && !file.isEmpty()) {
      String originalFilename = file.getOriginalFilename();
      if (originalFilename == null || !originalFilename.matches(".*\\.(png|jpg|jpeg)$")) {
        throw new AppEx(ErrorCode.INVALID_FILE_TYPE);
      }
      String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
      String fileName = dto.getRoomName().replaceAll("[^a-zA-Z0-9.-]", "_") + fileExtension;
      String s3Key = "rooms/" + fileName;
      String fileUrl = s3Service.uploadFile(s3Key, file);
      room.setImageUrl(fileUrl);
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
