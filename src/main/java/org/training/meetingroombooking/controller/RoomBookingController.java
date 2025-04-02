package org.training.meetingroombooking.controller;

import jakarta.validation.Valid;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.training.meetingroombooking.entity.dto.Response.ApiResponse;
import org.training.meetingroombooking.entity.dto.Response.UserResponse;
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
  public ApiResponse<RoomBookingDTO> createRoomBooking(@Valid @RequestBody RoomBookingDTO dto) {
    return ApiResponse.<RoomBookingDTO>builder()
        .success(true)
        .data(roomBookingService.create(dto))
        .build();
  }

  @GetMapping("/search")
  public ApiResponse<Page<RoomBookingDTO>> searchUsers(
      @RequestParam(value = "roomId", required = false) Long roomId,
      @RequestParam(value = "bookedById", required = false) Long bookedById,
      @RequestParam(value = "page", defaultValue = "0") int page,
      @RequestParam(value = "size", defaultValue = "10") int size
  ) {
    Page<RoomBookingDTO> roombookingsPage = roomBookingService.getRoomBookings(roomId, bookedById, page, size);
    return ApiResponse.<Page<RoomBookingDTO>>builder()
        .success(true)
        .data(roombookingsPage)
        .build();
  }

  @GetMapping
  public ApiResponse<List<RoomBookingDTO>> getRoomBookings() {
    return ApiResponse.<List<RoomBookingDTO>>builder()
        .success(true)
        .data(roomBookingService.getAll())
        .build();
  }

  @GetMapping("/by-room-name")
  public ResponseEntity<List<RoomBookingDTO>> getBookingsByRoomName(@RequestParam String roomName) {
    List<RoomBookingDTO> bookings = roomBookingService.getBookingsByRoomName(roomName);
    return ResponseEntity.ok(bookings);
  }

  @GetMapping("/user/{userName}")
  public ApiResponse<List<RoomBookingDTO>> getBookingsByUserName(@PathVariable String userName) {
    return ApiResponse.<List<RoomBookingDTO>>builder()
        .success(true)
        .data(roomBookingService.getBookingsByUserName(userName))
        .build();
  }

  @GetMapping("/by-room-id/{roomId}")
  public ApiResponse<List<RoomBookingDTO>> getBookingsByRoomId(@PathVariable Long roomId) {
    List<RoomBookingDTO> bookings = roomBookingService.getBookingsByRoomId(roomId);
    return ApiResponse.<List<RoomBookingDTO>>builder()
            .success(true)
            .data(bookings)
            .build();
  }

  @GetMapping("/MyBookings")
  public ApiResponse<List<RoomBookingDTO>> getMyBookings() {
    return ApiResponse.<List<RoomBookingDTO>>builder()
        .success(true)
        .data(roomBookingService.getMyBookings())
        .build();
  }

  @PutMapping("/{bookingId}")
  public ApiResponse<RoomBookingDTO> updateRoomBooking(@PathVariable Long bookingId,
      @Valid @RequestBody RoomBookingDTO dto) {
    return ApiResponse.<RoomBookingDTO>builder()
        .success(true)
        .data(roomBookingService.update(bookingId, dto))
        .build();
  }

  @DeleteMapping("/{bookingId}")
  @PreAuthorize("hasRole('ADMIN')")
  public ApiResponse<String> deleteRoomBooking(@PathVariable Long bookingId) {
    roomBookingService.delete(bookingId);
    return ApiResponse.<String>builder()
        .success(true)
        .data("room booking has been deleted")
        .build();
  }

  @GetMapping("/upcoming")
  public ApiResponse<List<RoomBookingDTO>> getUpcomingBookings() {
    return ApiResponse.<List<RoomBookingDTO>>builder()
            .success(true)
            .data(roomBookingService.getUpcomingBookings())
            .build();
  }

  @GetMapping("/upcoming/user/{userName}")
  public ApiResponse<List<RoomBookingDTO>> getUpcomingBookingsByUserName(@PathVariable String userName) {
    return ApiResponse.<List<RoomBookingDTO>>builder()
            .success(true)
            .data(roomBookingService.getUpcomingBookingsByUserName(userName))
            .build();
  }

  @GetMapping("/upcoming/room/{roomId}")
  public ApiResponse<List<RoomBookingDTO>> getUpcomingBookingsByRoomId(@PathVariable Long roomId) {
    return ApiResponse.<List<RoomBookingDTO>>builder()
            .success(true)
            .data(roomBookingService.getUpcomingBookingsByRoomId(roomId))
            .build();
  }

  @GetMapping("/upcoming/my")
  public ApiResponse<List<RoomBookingDTO>> getMyUpcomingBookings() {
    return ApiResponse.<List<RoomBookingDTO>>builder()
            .success(true)
            .data(roomBookingService.getMyUpcomingBookings())
            .build();
  }

  // Endpoint hủy đặt phòng: đổi trạng thái booking thành CANCELLED
  @PutMapping("/cancel/{bookingId}")
  public ApiResponse<RoomBookingDTO> cancelRoomBooking(@PathVariable Long bookingId) {
    RoomBookingDTO cancelledBooking = roomBookingService.cancelBooking(bookingId);
    return ApiResponse.<RoomBookingDTO>builder()
            .success(true)
            .data(cancelledBooking)
            .build();
  }
}
