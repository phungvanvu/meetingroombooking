package org.training.meetingroombooking.controller;

import java.util.List;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.training.meetingroombooking.entity.dto.NotificationDTO;
import org.training.meetingroombooking.entity.dto.Response.ApiResponse;
import org.training.meetingroombooking.service.NotificationService;

@RestController
@RequestMapping("/notification")
public class NotificationController {

  private final NotificationService notificationService;

  public NotificationController(NotificationService notificationService) {
    this.notificationService = notificationService;
  }

  @PostMapping
  @PreAuthorize("hasRole('ADMIN')")
  public ApiResponse<NotificationDTO> create(@RequestBody NotificationDTO request) {
    return ApiResponse.<NotificationDTO>builder()
        .success(true)
        .data(notificationService.create(request))
        .build();
  }

  @GetMapping
  @PreAuthorize("hasRole('ADMIN')")
  public ApiResponse<List<NotificationDTO>> getNotifications(){
    return ApiResponse.<List<NotificationDTO>>builder()
        .success(true)
        .data(notificationService.getAll())
        .build();
  }

  @GetMapping("/{userId}")
  @PreAuthorize("hasRole('ADMIN')")
  public ApiResponse<List<NotificationDTO>> getNotifications(@PathVariable Long userId){
    return ApiResponse.<List<NotificationDTO>>builder()
        .success(true)
        .data(notificationService.getByUserId(userId))
        .build();
  }

  @DeleteMapping("/{notificationId}")
  @PreAuthorize("hasRole('ADMIN')")
  public ApiResponse<String> deleteNotification(@PathVariable Long notificationId){
    notificationService.delete(notificationId);
    return ApiResponse.<String>builder()
        .success(true)
        .data("Notification has been deleted")
        .build();
  }

}
