package org.training.meetingroombooking.controller;

import jakarta.validation.Valid;
import java.util.List;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
  public ApiResponse<RoomDTO> createRoom(@Valid @RequestBody RoomDTO dto) {
    return ApiResponse.<RoomDTO>builder()
        .success(true)
        .data(roomService.create(dto))
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
  public ApiResponse<RoomDTO> updateRoom(@PathVariable Long roomId, @Valid @RequestBody RoomDTO dto) {
    return ApiResponse.<RoomDTO>builder()
        .success(true)
        .data(roomService.update(roomId, dto))
        .build();
  }

  @DeleteMapping("/{roomId}")
  @PreAuthorize("hasRole('ADMIN')")
  public ApiResponse<String> deleteRoom(@PathVariable Long roomId) {
    return ApiResponse.<String>builder()
        .success(true)
        .data("Room has been deleted")
        .build();
  }
}
