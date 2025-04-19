package org.training.meetingroombooking.service.impl;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Subquery;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
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
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.training.meetingroombooking.entity.dto.RoomDTO;
import org.training.meetingroombooking.entity.enums.ErrorCode;
import org.training.meetingroombooking.entity.mapper.RoomMapper;
import org.training.meetingroombooking.entity.models.Equipment;
import org.training.meetingroombooking.entity.models.Room;
import org.training.meetingroombooking.entity.models.RoomEquipment;
import org.training.meetingroombooking.entity.models.RoomImage;
import org.training.meetingroombooking.exception.AppEx;
import org.training.meetingroombooking.repository.RoomBookingRepository;
import org.training.meetingroombooking.repository.RoomRepository;
import org.training.meetingroombooking.service.RoomService;
import org.training.meetingroombooking.service.S3Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {

  private final RoomRepository roomRepository;
  private final RoomMapper roomMapper;
  private final RoomBookingRepository bookingRepository;
  private final S3Service s3Service;

  @Override
  public RoomDTO create(RoomDTO dto, List<MultipartFile> files) throws IOException {
    Room room = roomMapper.toEntity(dto);
    linkEquipments(room);
    if (files != null && !files.isEmpty()) {
      for (MultipartFile file : files) {
        if (file.isEmpty()) continue;
        String fileName = dto.getRoomName() + "_" + System.currentTimeMillis() + ".jpg";
        String url = uploadRoomImage(fileName, file);
        RoomImage image = RoomImage.builder().url(url).build();
        room.addImage(image);
      }
    }
    Room saved = roomRepository.save(room);
    return roomMapper.toDTO(saved);
  }

  @Override
  public RoomDTO findById(Long roomId) {
    return roomRepository.findById(roomId)
            .map(roomMapper::toDTO)
            .orElseThrow(() -> new AppEx(ErrorCode.ROOM_NOT_FOUND));
  }

  @Override
  public List<RoomDTO> getAll() {
    return roomRepository.findAll().stream()
            .map(roomMapper::toDTO)
            .collect(Collectors.toList());
  }

  @Override
  public List<RoomDTO> getAllAvailable() {
    return roomRepository.findAllByAvailableTrue().stream()
            .map(roomMapper::toDTO)
            .collect(Collectors.toList());
  }

  @Override
  public Page<RoomDTO> getRooms(
          String roomName,
          List<String> locations,
          Boolean available,
          List<Integer> capacities,
          Set<String> equipments,
          int page,
          int size) {
    Pageable pageable = PageRequest.of(page, size, Sort.by("roomId").descending());
    Specification<Room> spec = Specification.where(
            specByRoomName(roomName)
                    .and(specByLocations(locations))
                    .and(specByAvailable(available))
                    .and(specByCapacities(capacities))
                    .and(specByEquipments(equipments))
    );
    return roomRepository.findAll(spec, pageable)
            .map(roomMapper::toDTO);
  }

  @Override
  public Page<RoomDTO> getAvailableRooms(
          String roomName,
          List<String> locations,
          List<Integer> capacities,
          Set<String> equipments,
          int page,
          int size) {
    return getRooms(roomName, locations, true, capacities, equipments, page, size);
  }

  @Override
  public ByteArrayOutputStream exportRoomsToExcel() throws IOException {
    List<RoomDTO> rooms = getAll();

    try (InputStream template = getClass().getResourceAsStream("/templates/rooms_template.xlsx");
         Workbook wb = (template != null) ? new XSSFWorkbook(template) : new XSSFWorkbook()) {

      Sheet sheet = (wb.getNumberOfSheets() > 0) ? wb.getSheetAt(0) : wb.createSheet("Rooms");
      if (sheet.getPhysicalNumberOfRows() == 0) {
        createHeaderRow(sheet, wb);
      }
      populateRows(sheet, rooms);

      ByteArrayOutputStream out = new ByteArrayOutputStream();
      wb.write(out);
      return out;
    }
  }

  @Override
  public RoomDTO update(Long roomId, RoomDTO dto, List<MultipartFile> files) throws IOException {
    Room room = roomRepository.findById(roomId)
            .orElseThrow(() -> new AppEx(ErrorCode.ROOM_NOT_FOUND));
    roomMapper.updateRoom(room, dto);
    linkEquipments(room);
    if (files != null && !files.isEmpty()) {
      for (MultipartFile file : files) {
        if (file.isEmpty()) continue;
        String fileName = dto.getRoomName() + "_" + System.currentTimeMillis() + ".jpg";
        String url = uploadRoomImage(fileName, file);
        RoomImage image = RoomImage.builder().url(url).build();
        room.addImage(image);
      }
    }
    Room updated = roomRepository.save(room);
    return roomMapper.toDTO(updated);
  }

  @Override
  public void delete(Long roomId) {
    Room room = roomRepository.findById(roomId)
            .orElseThrow(() -> new AppEx(ErrorCode.ROOM_NOT_FOUND));

    if (bookingRepository.existsByRoom(room)) {
      throw new AppEx(ErrorCode.CANNOT_DELETE_ROOM_IN_USE);
    }
    roomRepository.delete(room);
  }

  @Override
  public void deleteMultipleRooms(List<Long> roomIds) {
    if (roomIds == null || roomIds.isEmpty()) {
      throw new AppEx(ErrorCode.INVALID_INPUT);
    }
    List<Room> rooms = roomRepository.findAllById(roomIds);
    if (rooms.size() != roomIds.size()) {
      throw new AppEx(ErrorCode.ROOM_NOT_FOUND);
    }
    rooms.forEach(r -> {
      if (bookingRepository.existsByRoom(r)) {
        throw new AppEx(ErrorCode.CANNOT_DELETE_ROOM_IN_USE);
      }
    });
    roomRepository.deleteAll(rooms);
  }

  // --- Private helpers ---

  private void linkEquipments(Room room) {
    if (room.getRoomEquipments() != null) {
      room.getRoomEquipments().forEach(re -> re.setRoom(room));
    }
  }

  private String uploadRoomImage(String roomName, MultipartFile file) throws IOException {
    String original = file.getOriginalFilename();
    if (original == null || !original.matches(".*\\.(png|jpg|jpeg)$")) {
      throw new AppEx(ErrorCode.INVALID_FILE_TYPE);
    }
    String ext = original.substring(original.lastIndexOf('.'));
    String safeName = roomName.replaceAll("[^a-zA-Z0-9.-]", "_") + ext;
    String key = "rooms/" + safeName;
    return s3Service.uploadFile(key, file);
  }

  private void createHeaderRow(Sheet sheet, Workbook wb) {
    String[] headers = {"Room Name", "Location", "Capacity", "Note", "Available"};
    Font font = wb.createFont();
    font.setBold(true);
    CellStyle style = wb.createCellStyle();
    style.setFont(font);

    Row row = sheet.createRow(0);
    for (int i = 0; i < headers.length; i++) {
      Cell cell = row.createCell(i);
      cell.setCellValue(headers[i]);
      cell.setCellStyle(style);
      sheet.autoSizeColumn(i);
    }
  }

  private void populateRows(Sheet sheet, List<RoomDTO> rooms) {
    int rowIndex = sheet.getLastRowNum() + 1;
    for (RoomDTO r : rooms) {
      Row row = sheet.createRow(rowIndex++);
      row.createCell(0).setCellValue(Optional.ofNullable(r.getRoomName()).orElse("N/A"));
      row.createCell(1).setCellValue(Optional.ofNullable(r.getLocation()).orElse("N/A"));
      row.createCell(2).setCellValue(r.getCapacity() != null ? r.getCapacity() : 0);
      row.createCell(3).setCellValue(Optional.ofNullable(r.getNote()).orElse("N/A"));
      row.createCell(4).setCellValue(r.isAvailable());
    }
  }

  private Specification<Room> specByRoomName(String name) {
    return (root, query, cb) ->
            (name == null || name.isBlank())
                    ? cb.conjunction()
                    : cb.like(cb.lower(root.get("roomName")), "%" + name.toLowerCase() + "%");
  }

  private Specification<Room> specByLocations(List<String> locs) {
    return (root, query, cb) ->
            (locs == null || locs.isEmpty())
                    ? cb.conjunction()
                    : root.get("location").in(locs);
  }

  private Specification<Room> specByAvailable(Boolean avail) {
    return (root, query, cb) ->
            (avail == null)
                    ? cb.conjunction()
                    : cb.equal(root.get("available"), avail);
  }

  private Specification<Room> specByCapacities(List<Integer> caps) {
    return (root, query, cb) ->
            (caps == null || caps.isEmpty())
                    ? cb.conjunction()
                    : root.get("capacity").in(caps);
  }

  private Specification<Room> specByEquipments(Set<String> eqs) {
    return (root, query, cb) -> {
      if (eqs == null || eqs.isEmpty()) {
        return cb.conjunction();
      }
      query.distinct(true);
      List<Predicate> predicates = new ArrayList<>();
      for (String equipmentName : eqs) {
        Join<Room, RoomEquipment> reJoin = root.join("roomEquipments", JoinType.INNER);
        Join<RoomEquipment, Equipment> eJoin = reJoin.join("equipment", JoinType.INNER);
        predicates.add(cb.equal(eJoin.get("equipmentName"), equipmentName));
      }

      return cb.and(predicates.toArray(new Predicate[0]));
    };
  }
}
