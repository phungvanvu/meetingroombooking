package org.training.meetingroombooking.controller;

import jakarta.validation.Valid;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
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
  public ApiResponse<RoomBookingDTO> createRoomBooking(@Valid @RequestBody RoomBookingDTO dto) {
    return ApiResponse.<RoomBookingDTO>builder()
        .success(true)
        .data(roomBookingService.create(dto))
        .build();
  }

  @GetMapping
  public ApiResponse<List<RoomBookingDTO>> getRoomBookings() {
    return ApiResponse.<List<RoomBookingDTO>>builder()
        .success(true)
        .data(roomBookingService.getAll())
        .build();
  }

  @GetMapping("/user/{userName}")
  public ApiResponse<List<RoomBookingDTO>> getBookingsByUserName(@PathVariable String userName) {
    return ApiResponse.<List<RoomBookingDTO>>builder()
            .success(true)
            .data(roomBookingService.getBookingsByUserName(userName))
            .build();
  }

  @GetMapping("/MyBookings")
  @PostAuthorize("returnObject.data.userName == authentication.name")
  public ApiResponse<List<RoomBookingDTO>> getMyBookings() {
    return ApiResponse.<List<RoomBookingDTO>>builder()
            .success(true)
            .data(roomBookingService.getMyBookings())
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

  @GetMapping("/export-excel")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<byte[]> exportBookingsToExcel() throws IOException {
    ByteArrayOutputStream outputStream = roomBookingService.exportBookingsToExcel();
    return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=bookings.xlsx")
            .contentType(MediaType.APPLICATION_OCTET_STREAM)
            .body(outputStream.toByteArray());
  }

  @GetMapping("/monthly-bookings/{month}/{year}")
  @PreAuthorize("hasRole('ADMIN')")
  public ApiResponse<Long> getMonthlyBookingCount(@PathVariable int month, @PathVariable int year) {
    return ApiResponse.<Long>builder()
            .success(true)
            .data(roomBookingService.getMonthlyBookingCount(month, year))
            .build();
  }

  @GetMapping("/current-month-bookings")
  @PreAuthorize("hasRole('ADMIN')")
  public ApiResponse<Long> getCurrentMonthBookingCount() {
    return ApiResponse.<Long>builder()
            .success(true)
            .data(roomBookingService.getCurrentMonthBookingCount())
            .build();
  }

}
