package org.training.meetingroombooking.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.training.meetingroombooking.entity.dto.RoomDTO;
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
      String uploadDir = "uploads/rooms";
      File uploadPath = new File(uploadDir);
      if (!uploadPath.exists()) {
        uploadPath.mkdirs();
      }
      Path filePath = Paths.get(uploadDir, fileName);
      Files.write(filePath, file.getBytes());
      room.setImageUrl("/uploads/" + fileName);
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
      String uploadDir = "uploads/";
      File uploadPath = new File(uploadDir);
      if (!uploadPath.exists()) {
        uploadPath.mkdirs();
      }
      Path filePath = Paths.get(uploadDir, fileName);
      Files.write(filePath, file.getBytes());
      room.setImageUrl("/uploads/" + fileName);
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
}
