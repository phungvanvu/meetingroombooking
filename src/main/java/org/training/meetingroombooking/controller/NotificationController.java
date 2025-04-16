package org.training.meetingroombooking.controller;

import jakarta.validation.Valid;
import java.util.List;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.training.meetingroombooking.entity.dto.NotificationDTO;
import org.training.meetingroombooking.entity.dto.Response.ApiResponse;
import org.training.meetingroombooking.service.NotificationService;

@RestController
@RequestMapping("/notification/v1.0")
public class NotificationController {

  private final NotificationService notificationService;

  public NotificationController(NotificationService notificationService) {
    this.notificationService = notificationService;
  }

  @PostMapping
  @PreAuthorize("hasRole('ADMIN')")
  public ApiResponse<NotificationDTO> create(@Valid @RequestBody NotificationDTO request) {
    return ApiResponse.<NotificationDTO>builder()
        .success(true)
        .data(notificationService.create(request))
        .build();
  }

  @GetMapping
  @PreAuthorize("hasRole('ADMIN')")
  public ApiResponse<List<NotificationDTO>> getNotifications() {
    return ApiResponse.<List<NotificationDTO>>builder()
        .success(true)
        .data(notificationService.getAll())
        .build();
  }

  @GetMapping("/MyNotification")
  public ApiResponse<List<NotificationDTO>> getMyNotifications() {
    return ApiResponse.<List<NotificationDTO>>builder()
        .success(true)
        .data(notificationService.getMyNotifications())
        .build();
  }

  @GetMapping("/{userName}")
  @PostAuthorize("hasRole('ADMIN')")
  public ApiResponse<List<NotificationDTO>> getNotifications(@Valid @PathVariable String userName) {
    return ApiResponse.<List<NotificationDTO>>builder()
        .success(true)
        .data(notificationService.getNotificationsByUserName(userName))
        .build();
  }

  @PutMapping("/{notificationId}")
  @PreAuthorize("hasRole('ADMIN')")
  public ApiResponse<NotificationDTO> updateNotification(
      @PathVariable Long notificationId, @RequestBody NotificationDTO request) {
    return ApiResponse.<NotificationDTO>builder()
        .success(true)
        .data(notificationService.update(notificationId, request))
        .build();
  }

  @DeleteMapping("/{notificationId}")
  @PreAuthorize("hasRole('ADMIN')")
  public ApiResponse<String> deleteNotification(@PathVariable Long notificationId) {
    notificationService.delete(notificationId);
    return ApiResponse.<String>builder()
        .success(true)
        .data("Notification has been deleted")
        .build();
  }
}
