package org.training.meetingroombooking.controller;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.training.meetingroombooking.entity.dto.Response.ApiResponse;
import org.training.meetingroombooking.entity.dto.Summary.BookingSummaryDTO;
import org.training.meetingroombooking.entity.dto.Summary.RoomStatisticsDTO;
import org.training.meetingroombooking.entity.dto.Summary.RoomSummaryDTO;
import org.training.meetingroombooking.entity.dto.Summary.UserSummaryDTO;
import org.training.meetingroombooking.service.StatisticalService;

@RestController
@RequestMapping("/api/v1/statistical")
public class StatisticalController {

  private final StatisticalService statisticalService;

  public StatisticalController(StatisticalService statisticalService) {
    this.statisticalService = statisticalService;
  }

  // Top người dùng đặt phòng nhiều nhất
  @GetMapping("/top-users/{limit}")
  @PreAuthorize("hasRole('ADMIN')")
  public ApiResponse<List<UserSummaryDTO>> getTopUsers(@PathVariable int limit) {
    return ApiResponse.<List<UserSummaryDTO>>builder()
        .success(true)
        .data(statisticalService.getTopUsers(limit))
        .build();
  }

  @GetMapping("/statistics")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<ApiResponse<RoomStatisticsDTO>> getRoomStatistics() {
    RoomStatisticsDTO stats = statisticalService.getRoomStatistics();
    ApiResponse<RoomStatisticsDTO> response =
        ApiResponse.<RoomStatisticsDTO>builder().success(true).data(stats).build();
    return ResponseEntity.ok(response);
  }

  // Endpoint: Lấy phòng được đặt nhiều nhất
  @GetMapping("/most-booked-room")
  @PreAuthorize("hasRole('ADMIN')")
  public ApiResponse<RoomSummaryDTO> getMostBookedRoom() {
    return ApiResponse.<RoomSummaryDTO>builder()
        .success(true)
        .data(statisticalService.getMostBookedRoom())
        .build();
  }

  // Endpoint: Lấy số lượt đặt phòng theo quý
  @GetMapping("/quarterly-bookings")
  @PreAuthorize("hasRole('ADMIN')")
  public ApiResponse<List<BookingSummaryDTO>> getQuarterlyBookings() {
    return ApiResponse.<List<BookingSummaryDTO>>builder()
        .success(true)
        .data(statisticalService.getQuarterlyBookings())
        .build();
  }

  // Endpoint: Lấy số lượt đặt phòng theo tuần
  @GetMapping("/weekly-bookings")
  @PreAuthorize("hasRole('ADMIN')")
  public ApiResponse<List<BookingSummaryDTO>> getWeeklyBookings() {
    return ApiResponse.<List<BookingSummaryDTO>>builder()
        .success(true)
        .data(statisticalService.getWeeklyBookings())
        .build();
  }

  // Endpoint: Lấy số lượt đặt phòng theo tháng
  @GetMapping("/monthly-bookings")
  @PreAuthorize("hasRole('ADMIN')")
  public ApiResponse<List<BookingSummaryDTO>> getMonthlyBookings() {
    return ApiResponse.<List<BookingSummaryDTO>>builder()
        .success(true)
        .data(statisticalService.getMonthlyBookings())
        .build();
  }

  // Endpoint: Lấy số lượt đặt phòng trong tháng hiện tại
  @GetMapping("/current-month-bookings")
  @PreAuthorize("hasRole('ADMIN')")
  public ApiResponse<BookingSummaryDTO> getCurrentMonthBookings() {
    return ApiResponse.<BookingSummaryDTO>builder()
        .success(true)
        .data(statisticalService.getCurrentMonthBookings())
        .build();
  }
}
