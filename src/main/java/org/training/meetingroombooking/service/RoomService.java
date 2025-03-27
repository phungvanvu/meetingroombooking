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
      room.setImageUrl("/uploads/rooms/" + fileName);
    }
    Room savedRoom = roomRepository.save(room);
    return roomMapper.toDTO(savedRoom);
  }

  public List<RoomDTO> findByNames(List<String> roomNames) {
    List<Room> rooms = roomRepository.findByRoomNames(roomNames);
    if (rooms.isEmpty()) {
      throw new AppEx(ErrorCode.ROOM_NOT_FOUND);
    }
    return rooms.stream().map(roomMapper::toDTO).collect(Collectors.toList());
  }

  public List<RoomDTO> findByCapacity(List<Integer> capacity) {
    List<Room> rooms = roomRepository.findByCapacity(capacity);
    if (rooms.isEmpty()) {
      throw new AppEx(ErrorCode.ROOM_NOT_FOUND);
    }
    return rooms.stream().map(roomMapper::toDTO).collect(Collectors.toList());
  }

  public List<RoomDTO> findByLocation(List<String> location) {
    List<Room> rooms = roomRepository.findByLocation(location);
    if (rooms.isEmpty()) {
      throw new AppEx(ErrorCode.ROOM_NOT_FOUND);
    }
    return rooms.stream().map(roomMapper::toDTO).collect(Collectors.toList());
  }

  public List<RoomDTO> findByNameAndLocation(List<String> roomName, List<String> location) {
    List<Room> rooms = roomRepository.findByRoomNameAndLocation(roomName, location);
    if (rooms.isEmpty()) {
      throw new AppEx(ErrorCode.ROOM_NOT_FOUND);
    }
    return rooms.stream().map(roomMapper::toDTO).collect(Collectors.toList());
  }

  public List<RoomDTO> findByNameAndCapacity(List<String> roomName, List<Integer> capacity) {
    List<Room> rooms = roomRepository.findByRoomNameAndCapacity(roomName, capacity);
    if (rooms.isEmpty()) {
      throw new AppEx(ErrorCode.ROOM_NOT_FOUND);
    }
    return rooms.stream().map(roomMapper::toDTO).collect(Collectors.toList());
  }

  public List<RoomDTO> findByCapacityAndLocation(List<Integer> capacity, List<String> location) {
    List<Room> rooms = roomRepository.findByCapacityAndLocation(capacity, location);
    if (rooms.isEmpty()) {
      throw new AppEx(ErrorCode.ROOM_NOT_FOUND);
    }
    return rooms.stream().map(roomMapper::toDTO).collect(Collectors.toList());
  }

  public List<RoomDTO> findByNameAndCapacityAndLocation(List<String> roomName,
      List<Integer> capacity, List<String> location) {
    List<Room> rooms = roomRepository.findByRoomNameAndCapacityAndLocation(roomName, capacity,
        location);
    if (rooms.isEmpty()) {
      throw new AppEx(ErrorCode.ROOM_NOT_FOUND);
    }
    return rooms.stream().map(roomMapper::toDTO).collect(Collectors.toList());
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
      String uploadDir = "uploads/rooms";
      File uploadPath = new File(uploadDir);
      if (!uploadPath.exists()) {
        uploadPath.mkdirs();
      }
      Path filePath = Paths.get(uploadDir, fileName);
      Files.write(filePath, file.getBytes());
      room.setImageUrl("/uploads/rooms" + fileName);
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

}
