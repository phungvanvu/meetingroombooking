package org.training.meetingroombooking.controller;

import jakarta.validation.Valid;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.training.meetingroombooking.entity.dto.Response.ApiResponse;
import org.training.meetingroombooking.entity.dto.RoomBookingDTO;
import org.training.meetingroombooking.entity.enums.BookingStatus;
import org.training.meetingroombooking.service.RoomBookingService;

@RestController
@RequestMapping("/api/v1/roombooking")
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

  @GetMapping
  @PreAuthorize("hasRole('ADMIN')")
  public ApiResponse<List<RoomBookingDTO>> getRoomBookings() {
    return ApiResponse.<List<RoomBookingDTO>>builder()
        .success(true)
        .data(roomBookingService.getAll())
        .build();
  }

  @GetMapping("/user/{userName}")
  @PreAuthorize("hasRole('ADMIN')")
  public ApiResponse<List<RoomBookingDTO>> getBookingsByUserName(@PathVariable String userName) {
    return ApiResponse.<List<RoomBookingDTO>>builder()
        .success(true)
        .data(roomBookingService.getBookingsByUserName(userName))
        .build();
  }

  @GetMapping("/by-room-id/{roomId}")
  @PreAuthorize("hasRole('ADMIN')")
  public ApiResponse<List<RoomBookingDTO>> getBookingsByRoomId(@PathVariable Long roomId) {
    List<RoomBookingDTO> bookings = roomBookingService.getBookingsByRoomId(roomId);
    return ApiResponse.<List<RoomBookingDTO>>builder().success(true).data(bookings).build();
  }

  @PutMapping("/{bookingId}")
  public ApiResponse<RoomBookingDTO> updateRoomBooking(
      @PathVariable Long bookingId, @Valid @RequestBody RoomBookingDTO dto) {
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
  @PreAuthorize("hasRole('ADMIN')")
  public ApiResponse<List<RoomBookingDTO>> getUpcomingBookings() {
    return ApiResponse.<List<RoomBookingDTO>>builder()
        .success(true)
        .data(roomBookingService.getUpcomingBookings())
        .build();
  }

  @GetMapping("/upcoming/user/{userName}")
  @PreAuthorize("hasRole('ADMIN')")
  public ApiResponse<List<RoomBookingDTO>> getUpcomingBookingsByUserName(
      @PathVariable String userName) {
    return ApiResponse.<List<RoomBookingDTO>>builder()
        .success(true)
        .data(roomBookingService.getUpcomingBookingsByUserName(userName))
        .build();
  }

  @GetMapping("/upcoming/room/{roomId}")
  @PreAuthorize("hasRole('ADMIN')")
  public ApiResponse<List<RoomBookingDTO>> getUpcomingBookingsByRoomId(@PathVariable Long roomId) {
    return ApiResponse.<List<RoomBookingDTO>>builder()
        .success(true)
        .data(roomBookingService.getUpcomingBookingsByRoomId(roomId))
        .build();
  }

  /**
   * Lấy danh sách các booking sắp tới (startTime > hiện tại) của người dùng hiện tại. Hỗ trợ lọc
   * theo: - roomName: tên phòng - fromTime: thời gian bắt đầu từ - toTime: thời gian kết thúc đến -
   * Phân trang, sắp xếp theo bookingId giảm dần
   */
  @GetMapping("/upcoming/my")
  public ApiResponse<Page<RoomBookingDTO>> getMyUpcomingBookings(
      @RequestParam(value = "roomName", required = false) String roomName,
      @RequestParam(value = "fromTime", required = false)
          @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
          LocalDateTime fromTime,
      @RequestParam(value = "toTime", required = false)
          @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
          LocalDateTime toTime,
      @RequestParam(value = "page", defaultValue = "0") int page,
      @RequestParam(value = "size", defaultValue = "10") int size) {

    Page<RoomBookingDTO> bookings =
        roomBookingService.getMyUpcomingBookings(roomName, fromTime, toTime, page, size);
    return ApiResponse.<Page<RoomBookingDTO>>builder().success(true).data(bookings).build();
  }

  // Endpoint hủy đặt phòng: đổi trạng thái booking thành CANCELLED
  @PutMapping("/cancel/{bookingId}")
  public ApiResponse<RoomBookingDTO> cancelRoomBooking(@PathVariable Long bookingId) {
    RoomBookingDTO cancelledBooking = roomBookingService.cancelBooking(bookingId);
    return ApiResponse.<RoomBookingDTO>builder().success(true).data(cancelledBooking).build();
  }

  // Endpoint hủy đặt phòng đồng loạt: đổi trạng thái booking thành CANCELLED
  @PutMapping("/cancel-multiple")
  public ApiResponse<String> cancelMultipleBookings(@RequestBody List<Long> bookingIds) {
    roomBookingService.cancelMultipleBookings(bookingIds);
    return ApiResponse.<String>builder()
        .success(true)
        .data("Selected bookings have been canceled successfully.")
        .build();
  }

  /**
   * Lấy danh sách RoomBooking của người dùng hiện hành theo các tiêu chí: - roomName: tìm kiếm theo
   * tên phòng (không phân biệt chữ hoa chữ thường) - fromTime: thời gian bắt đầu (startTime) từ -
   * toTime: thời gian kết thúc (endTime) đến - status: trạng thái đặt phòng - Phân trang theo thứ
   * tự giảm dần của bookingId
   */
  @GetMapping("/search-my")
  public ApiResponse<Page<RoomBookingDTO>> searchMyBookings(
      @RequestParam(value = "roomName", required = false) String roomName,
      @RequestParam(value = "fromTime", required = false)
          @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
          LocalDateTime fromTime,
      @RequestParam(value = "toTime", required = false)
          @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
          LocalDateTime toTime,
      @RequestParam(value = "status", required = false) BookingStatus status,
      @RequestParam(value = "page", defaultValue = "0") int page,
      @RequestParam(value = "size", defaultValue = "10") int size) {

    Page<RoomBookingDTO> bookingsPage =
        roomBookingService.searchMyBookings(roomName, fromTime, toTime, status, page, size);
    return ApiResponse.<Page<RoomBookingDTO>>builder().success(true).data(bookingsPage).build();
  }

  /**
   * Endpoint tìm kiếm toàn bộ bản ghi booking theo các tiêu chí: - roomName: tên phòng (không phân
   * biệt chữ hoa chữ thường) - fromTime: thời gian bắt đầu (startTime) từ - toTime: thời gian kết
   * thúc (endTime) đến - status: trạng thái booking - bookedById: filter theo userId của người đặt
   * - Phân trang, sắp xếp theo bookingId giảm dần Endpoint này dành cho Admin.
   */
  @GetMapping("/search")
  @PreAuthorize("hasRole('ADMIN')")
  public ApiResponse<Page<RoomBookingDTO>> searchBookings(
      @RequestParam(value = "roomName", required = false) String roomName,
      @RequestParam(value = "fromTime", required = false)
          @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
          LocalDateTime fromTime,
      @RequestParam(value = "toTime", required = false)
          @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
          LocalDateTime toTime,
      @RequestParam(value = "status", required = false) BookingStatus status,
      @RequestParam(value = "bookedByName", required = false) String bookedByName,
      @RequestParam(value = "page", defaultValue = "0") int page,
      @RequestParam(value = "size", defaultValue = "10") int size) {

    Page<RoomBookingDTO> bookings =
        roomBookingService.searchBookings(
            roomName, fromTime, toTime, status, bookedByName, page, size);
    return ApiResponse.<Page<RoomBookingDTO>>builder().success(true).data(bookings).build();
  }

  @GetMapping("/export-bookings-excel")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<byte[]> exportBookingsToExcel() throws IOException {
    ByteArrayOutputStream outputStream = roomBookingService.exportBookingsToExcel();
    String currentTime = LocalTime.now().toString().replace(":", "-");
    String fileName = "bookings_export_" + LocalDate.now() + "_" + currentTime + ".xlsx";
    return ResponseEntity.ok()
        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName)
        .contentType(MediaType.APPLICATION_OCTET_STREAM)
        .body(outputStream.toByteArray());
  }
}
