package org.training.meetingroombooking.controller;

import jakarta.validation.Valid;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.training.meetingroombooking.entity.dto.Response.ApiResponse;
import org.training.meetingroombooking.entity.dto.RoomDTO;
import org.training.meetingroombooking.service.RoomService;

@RestController
@RequestMapping("/api/v1/room")
public class RoomController {

  private final RoomService roomService;

  public RoomController(RoomService roomService) {
    this.roomService = roomService;
  }

  @PostMapping
  @PreAuthorize("hasRole('ADMIN')")
  public ApiResponse<RoomDTO> createRoom(
      @RequestPart("room") @Valid RoomDTO dto,
      @RequestPart(value = "file", required = false) MultipartFile file)
      throws IOException {
    return ApiResponse.<RoomDTO>builder().success(true).data(roomService.create(dto, file)).build();
  }

  @GetMapping
  public ApiResponse<List<RoomDTO>> getRooms() {
    return ApiResponse.<List<RoomDTO>>builder().success(true).data(roomService.getAll()).build();
  }

  @GetMapping("/available/all")
  public ApiResponse<List<RoomDTO>> getAvailableRooms() {
    return ApiResponse.<List<RoomDTO>>builder()
        .success(true)
        .data(roomService.getAllAvailable())
        .build();
  }

  @GetMapping("/search")
  public ApiResponse<Page<RoomDTO>> searchRooms(
      @RequestParam(value = "roomName", required = false) String roomName,
      @RequestParam(value = "locations", required = false) List<String> locations,
      @RequestParam(value = "available", required = false) Boolean available,
      @RequestParam(value = "capacities", required = false) List<Integer> capacities,
      @RequestParam(value = "equipments", required = false) Set<String> equipments,
      @RequestParam(value = "page", defaultValue = "0") int page,
      @RequestParam(value = "size", defaultValue = "6") int size) {
    Page<RoomDTO> roomsPage =
        roomService.getRooms(roomName, locations, available, capacities, equipments, page, size);
    return ApiResponse.<Page<RoomDTO>>builder().success(true).data(roomsPage).build();
  }

  @GetMapping("/available")
  public ApiResponse<Page<RoomDTO>> getAvailableRooms(
      @RequestParam(value = "roomName", required = false) String roomName,
      @RequestParam(value = "locations", required = false) List<String> locations,
      @RequestParam(value = "capacities", required = false) List<Integer> capacities,
      @RequestParam(value = "equipments", required = false) Set<String> equipments,
      @RequestParam(value = "page", defaultValue = "0") int page,
      @RequestParam(value = "size", defaultValue = "6") int size) {
    Page<RoomDTO> roomsPage =
        roomService.getAvailableRooms(roomName, locations, capacities, equipments, page, size);
    return ApiResponse.<Page<RoomDTO>>builder().success(true).data(roomsPage).build();
  }

  @GetMapping("/{roomId}")
  public ApiResponse<RoomDTO> getRoomById(@PathVariable Long roomId) {
    return ApiResponse.<RoomDTO>builder().success(true).data(roomService.findById(roomId)).build();
  }

  @PutMapping("/{roomId}")
  @PreAuthorize("hasRole('ADMIN')")
  public ApiResponse<RoomDTO> updateRoom(
      @PathVariable Long roomId,
      @RequestPart("room") @Valid RoomDTO dto,
      @RequestPart(value = "file", required = false) MultipartFile file)
      throws IOException {
    return ApiResponse.<RoomDTO>builder()
        .success(true)
        .data(roomService.update(roomId, dto, file))
        .build();
  }

  @DeleteMapping("/{roomId}")
  @PreAuthorize("hasRole('ADMIN')")
  public ApiResponse<String> deleteRoom(@PathVariable Long roomId) {
    roomService.delete(roomId);
    return ApiResponse.<String>builder().success(true).data("Room has been deleted").build();
  }

  @DeleteMapping("/delete-multiple")
  @PreAuthorize("hasRole('ADMIN')")
  public ApiResponse<String> deleteMultipleRoom(@RequestBody List<Long> roomIds) {
    roomService.deleteMultipleRooms(roomIds);
    return ApiResponse.<String>builder()
        .success(true)
        .data("Selected rooms have been deleted successfully.")
        .build();
  }

  @GetMapping("/export-rooms-excel")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<byte[]> exportRoomsToExcel() throws IOException {
    ByteArrayOutputStream outputStream = roomService.exportRoomsToExcel();
    String currentTime = LocalTime.now().toString().replace(":", "-");
    String fileName = "rooms_export_" + LocalDate.now() + "_" + currentTime + ".xlsx";
    return ResponseEntity.ok()
        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName)
        .contentType(MediaType.APPLICATION_OCTET_STREAM)
        .body(outputStream.toByteArray());
  }
}
