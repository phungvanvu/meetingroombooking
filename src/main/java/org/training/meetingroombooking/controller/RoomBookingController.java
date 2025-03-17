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
import org.training.meetingroombooking.entity.dto.RoomBookingDTO;
import org.training.meetingroombooking.service.RoomBookingService;

@RestController
@RequestMapping("/roombooking")
public class RoomBookingController {

  private final RoomBookingService roomBookingService;

  public RoomBookingController(RoomBookingService roomBookingService) {
    this.roomBookingService = roomBookingService;
  }

  @PostMapping
  @PreAuthorize("hasRole('ADMIN')")
  public ApiResponse<RoomBookingDTO> createRoom(@Valid @RequestBody RoomBookingDTO dto) {
    return ApiResponse.<RoomBookingDTO>builder()
        .success(true)
        .data(roomBookingService.create(dto))
        .build();
  }

  @GetMapping
  @PreAuthorize("hasRole('ADMIN')")
  public ApiResponse<List<RoomBookingDTO>> getRoomBookings() {
    return ApiResponse.<List<RoomBookingDTO>>builder()
        .success(true)
        .data(roomBookingService.getAll())
        .build();
  }

  @PutMapping("/{bookingId}")
  @PreAuthorize("hasRole('ADMIN')")
  public ApiResponse<RoomBookingDTO> updateRoomBooking(@PathVariable Long bookingId, @Valid @RequestBody RoomBookingDTO dto) {
    return ApiResponse.<RoomBookingDTO>builder()
        .success(true)
        .data(roomBookingService.update(bookingId, dto))
        .build();
  }

  @DeleteMapping("/{bookingId}")
  @PreAuthorize("hasRole('ADMIN')")
  public ApiResponse<String> deleteRoomBooking(@PathVariable Long bookingId) {
    return ApiResponse.<String>builder()
        .success(true)
        .data("room booking has been deleted")
        .build();
  }
}
