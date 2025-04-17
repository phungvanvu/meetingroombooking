package org.training.meetingroombooking.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;
import org.training.meetingroombooking.entity.dto.RoomDTO;

public interface RoomService {
  RoomDTO create(RoomDTO dto, MultipartFile file) throws IOException;

  RoomDTO findById(Long roomId);

  List<RoomDTO> getAll();

  List<RoomDTO> getAllAvailable();

  Page<RoomDTO> getRooms(
      String roomName,
      List<String> locations,
      Boolean available,
      List<Integer> capacities,
      Set<String> equipments,
      int page,
      int size);

  Page<RoomDTO> getAvailableRooms(
      String roomName,
      List<String> locations,
      List<Integer> capacities,
      Set<String> equipments,
      int page,
      int size);

  ByteArrayOutputStream exportRoomsToExcel() throws IOException;

  RoomDTO update(Long roomId, RoomDTO dto, MultipartFile file) throws IOException;

  void delete(Long roomId);

  void deleteMultipleRooms(List<Long> roomIds);
}
