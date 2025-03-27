package org.training.meetingroombooking.controller;

import jakarta.validation.Valid;

import java.io.IOException;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.training.meetingroombooking.entity.dto.Response.ApiResponse;
import org.training.meetingroombooking.entity.dto.RoomDTO;
import org.training.meetingroombooking.service.RoomService;

@RestController
@RequestMapping("/room")
public class RoomController {

  private final RoomService roomService;

  public RoomController(RoomService roomService) {
    this.roomService = roomService;
  }

  @PostMapping
  @PreAuthorize("hasRole('ADMIN')")
  public ApiResponse<RoomDTO> createRoom(
      @RequestPart("room") @Valid RoomDTO dto,
      @RequestPart(value = "file", required = false) MultipartFile file
  ) throws IOException {
    return ApiResponse.<RoomDTO>builder()
        .success(true)
        .data(roomService.create(dto, file))
        .build();
  }

  @GetMapping("/name/{roomName}")
  public ApiResponse<RoomDTO> getRoomByName(@PathVariable String roomName) {
    return ApiResponse.<RoomDTO>builder()
        .success(true)
        .data(roomService.findByRoomName(roomName))
        .build();
  }

  @GetMapping("/rooms/by-name/{roomNames}")
  public ApiResponse<List<RoomDTO>> getRoomsByRoomName(@PathVariable List<String> roomNames) {
    return ApiResponse.<List<RoomDTO>>builder()
        .success(true)
        .data(roomService.findByNames(roomNames))
        .build();
  }

  @GetMapping("/rooms/by-location/{location}")
  public ApiResponse<List<RoomDTO>> getRoomsByLocation(@PathVariable List<String> location) {
    return ApiResponse.<List<RoomDTO>>builder()
        .success(true)
        .data(roomService.findByLocation(location))
        .build();
  }

  @GetMapping("/rooms/by-name-and-location/{roomName}/{location}")
  public ApiResponse<List<RoomDTO>> getRoomsByLocationAndRoomName(
      @PathVariable List<String> roomName, @PathVariable List<String> location) {
    return ApiResponse.<List<RoomDTO>>builder()
        .success(true)
        .data(roomService.findByNameAndLocation(roomName, location))
        .build();
  }

  @GetMapping("/rooms/by-capacity/{capacity}")
  public ApiResponse<List<RoomDTO>> getRoomsByCapacity(@PathVariable List<Integer> capacity) {
    return ApiResponse.<List<RoomDTO>>builder()
        .success(true)
        .data(roomService.findByCapacity(capacity))
        .build();
  }

  @GetMapping("/rooms/by-name-and-capacity/{roomName}/{capacity}")
  public ApiResponse<List<RoomDTO>> getRoomsByRoomNameAndCapacity(
      @PathVariable List<String> roomName, @PathVariable List<Integer> capacity) {
    return ApiResponse.<List<RoomDTO>>builder()
        .success(true)
        .data(roomService.findByNameAndCapacity(roomName, capacity))
        .build();
  }

  @GetMapping("/rooms/by-name-capacity-location/{roomName}/{capacity}/{location}")
  public ApiResponse<List<RoomDTO>> getRoomsByRoomNameAndCapacityAndLocation(
      @PathVariable List<String> roomName, @PathVariable List<Integer> capacity,
      @PathVariable List<String> location) {
    return ApiResponse.<List<RoomDTO>>builder()
        .success(true)
        .data(roomService.findByNameAndCapacityAndLocation(roomName, capacity, location))
        .build();
  }

  @GetMapping("/rooms/by-capacity-location/{capacity}/{location}")
  public ApiResponse<List<RoomDTO>> getRoomsByCapacityAndLocation(
      @PathVariable List<Integer> capacity, @PathVariable List<String> location) {
    return ApiResponse.<List<RoomDTO>>builder()
        .success(true)
        .data(roomService.findByCapacityAndLocation(capacity, location))
        .build();
  }

  @GetMapping
  public ApiResponse<List<RoomDTO>> getRooms() {
    return ApiResponse.<List<RoomDTO>>builder()
        .success(true)
        .data(roomService.getAll())
        .build();
  }

  @GetMapping("/{roomId}")
  public ApiResponse<RoomDTO> getRoomById(@PathVariable Long roomId) {
    return ApiResponse.<RoomDTO>builder()
        .success(true)
        .data(roomService.findById(roomId))
        .build();
  }

  @PutMapping("/{roomId}")
  @PreAuthorize("hasRole('ADMIN')")
  public ApiResponse<RoomDTO> updateRoom(
      @PathVariable Long roomId,
      @RequestPart("room") @Valid RoomDTO dto,
      @RequestPart(value = "file", required = false) MultipartFile file
  ) throws IOException {
    return ApiResponse.<RoomDTO>builder()
        .success(true)
        .data(roomService.update(roomId, dto, file))
        .build();
  }


  @DeleteMapping("/{roomId}")
  @PreAuthorize("hasRole('ADMIN')")
  public ApiResponse<String> deleteRoom(
      @PathVariable Long roomId) {
    roomService.delete(roomId);
    return ApiResponse.<String>builder()
        .success(true)
        .data("Room has been deleted")
        .build();
  }
}
