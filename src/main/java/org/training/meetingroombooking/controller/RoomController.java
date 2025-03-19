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

  @GetMapping
  @PreAuthorize("hasRole('ADMIN')")
  public ApiResponse<List<RoomDTO>> getRooms() {
    return ApiResponse.<List<RoomDTO>>builder()
        .success(true)
        .data(roomService.getAll())
        .build();
  }

  @GetMapping("/{roomId}")
  @PreAuthorize("hasRole('ADMIN')")
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
    return ApiResponse.<String>builder()
        .success(true)
        .data("Room has been deleted")
        .build();
  }
}
