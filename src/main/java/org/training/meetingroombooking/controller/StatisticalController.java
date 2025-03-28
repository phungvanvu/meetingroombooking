package org.training.meetingroombooking.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.training.meetingroombooking.entity.dto.Response.ApiResponse;
import org.training.meetingroombooking.entity.dto.Summary.RoomStatisticsDTO;
import org.training.meetingroombooking.entity.dto.Summary.RoomSummaryDTO;
import org.training.meetingroombooking.entity.dto.Summary.UserSummaryDTO;
import org.training.meetingroombooking.service.RoomBookingService;
import org.training.meetingroombooking.service.RoomService;
import org.training.meetingroombooking.service.StatisticalService;

import java.util.List;
import org.training.meetingroombooking.service.UserService;

@RestController
@RequestMapping("/statistical")
public class StatisticalController {

    private final StatisticalService statisticalService;
    private final RoomBookingService roomBookingService;
    private final RoomService roomService;
    private final UserService userService;

    public StatisticalController(
        StatisticalService statisticalService,  RoomBookingService roomBookingService,
        RoomService roomService,  UserService userService) {
        this.statisticalService = statisticalService;
        this.roomBookingService = roomBookingService;
        this.roomService = roomService;
        this.userService = userService;
    }

    @GetMapping("/monthly-bookings/{month}/{year}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Long> getMonthlyBookingCount(@PathVariable int month, @PathVariable int year) {
        return ApiResponse.<Long>builder()
                .success(true)
                .data(statisticalService.getMonthlyBookingCount(month, year))
                .build();
    }

    @GetMapping("/current-month-bookings")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Long> getCurrentMonthBookingCount() {
        return ApiResponse.<Long>builder()
                .success(true)
                .data(statisticalService.getCurrentMonthBookingCount())
                .build();
    }

    @GetMapping("/most-booked-room")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<RoomSummaryDTO> getMostBookedRoomOfMonth() {
        return ApiResponse.<RoomSummaryDTO>builder()
                .success(true)
                .data(statisticalService.getMostBookedRoomOfMonth())
                .build();
    }

    // Tổng lượt đặt phòng theo tuần
    @GetMapping("/weekly-bookings/{week}/{year}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Long> getWeeklyBookingCount(@PathVariable int week, @PathVariable int year) {
        return ApiResponse.<Long>builder()
                .success(true)
                .data(statisticalService.getWeeklyBookingCount(week, year))
                .build();
    }

    // Tổng lượt đặt phòng theo quý
    @GetMapping("/quarterly-bookings/{quarter}/{year}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Long> getQuarterlyBookingCount(@PathVariable int quarter, @PathVariable int year) {
        return ApiResponse.<Long>builder()
                .success(true)
                .data(statisticalService.getQuarterlyBookingCount(quarter, year))
                .build();
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
    public ApiResponse<RoomStatisticsDTO> getRoomStatistics() {
        return ApiResponse.<RoomStatisticsDTO>builder()
                .success(true)
                .data(statisticalService.getRoomStatistics())
                .build();
    }
    @GetMapping("/export-bookings-excel")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<byte[]> exportBookingsToExcel() throws IOException {
        ByteArrayOutputStream outputStream = roomBookingService.exportBookingsToExcel();
        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=bookings.xlsx")
            .contentType(MediaType.APPLICATION_OCTET_STREAM)
            .body(outputStream.toByteArray());
    }

    @GetMapping("/export-rooms-excel")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<byte[]> exportRoomsToExcel() throws IOException {
        ByteArrayOutputStream outputStream = roomService.exportRoomsToExcel();
        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION,"attachment; filename=rooms.xlsx")
            .contentType(MediaType.APPLICATION_OCTET_STREAM)
            .body(outputStream.toByteArray());
    }

    @GetMapping("/export-user-excel")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<byte[]> exportUsersToExcel() throws IOException {
        ByteArrayOutputStream outputStream = userService.exportUserToExcel();
        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=users.xlsx")
            .contentType(MediaType.APPLICATION_OCTET_STREAM)
            .body(outputStream.toByteArray());
    }
}
