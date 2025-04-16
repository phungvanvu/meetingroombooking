package org.training.meetingroombooking.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.training.meetingroombooking.entity.dto.NotificationDTO;
import org.training.meetingroombooking.entity.enums.NotificationType;
import org.training.meetingroombooking.entity.mapper.NotificationMapper;
import org.training.meetingroombooking.entity.models.Notification;
import org.training.meetingroombooking.entity.models.User;
import org.training.meetingroombooking.exception.AppEx;
import org.training.meetingroombooking.repository.NotificationRepository;

class NotificationServiceTest {

  @Mock private NotificationRepository notificationRepository;

  @Mock private NotificationMapper notificationMapper;

  @InjectMocks private NotificationService notificationService;

  private Notification notification;
  private NotificationDTO notificationDTO;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);

    notification =
        Notification.builder()
            .notificationId(1L)
            .content("Test Notification")
            .type(NotificationType.INFO)
            .hasRead(false)
            .user(new User())
            .createdAt(LocalDateTime.now())
            .build();

    notificationDTO =
        NotificationDTO.builder()
            .content("Test Notification")
            .type(NotificationType.INFO)
            .isRead(false)
            .userId(1L)
            .createdAt(LocalDateTime.now())
            .build();
  }

  @Test
  void ***REMOVED***CreateNotification() {
    when(notificationMapper.toEntity(notificationDTO)).thenReturn(notification);
    when(notificationRepository.save(any(Notification.class))).thenReturn(notification);
    when(notificationMapper.toDTO(notification)).thenReturn(notificationDTO);

    NotificationDTO result = notificationService.create(notificationDTO);

    assertNotNull(result);
    assertEquals("Test Notification", result.getContent());
    verify(notificationRepository, times(1)).save(any(Notification.class));
  }

  @Test
  void ***REMOVED***GetAllNotifications() {
    List<Notification> notifications = Arrays.asList(notification);
    List<NotificationDTO> notificationDTOs = Arrays.asList(notificationDTO);

    when(notificationRepository.findAll()).thenReturn(notifications);
    when(notificationMapper.toDTO(any(Notification.class))).thenReturn(notificationDTO);

    List<NotificationDTO> result = notificationService.getAll();

    assertNotNull(result);
    assertEquals(1, result.size());
    assertEquals("Test Notification", result.get(0).getContent());
  }

  @Test
  void ***REMOVED***GetNotificationsByUserName() {
    List<Notification> notifications = Arrays.asList(notification);
    List<NotificationDTO> notificationDTOs = Arrays.asList(notificationDTO);

    when(notificationRepository.findByUser_UserName("***REMOVED***User")).thenReturn(notifications);
    when(notificationMapper.toDTO(any(Notification.class))).thenReturn(notificationDTO);

    List<NotificationDTO> result = notificationService.getNotificationsByUserName("***REMOVED***User");

    assertNotNull(result);
    assertEquals(1, result.size());
    assertEquals("Test Notification", result.get(0).getContent());
  }

  @Test
  void ***REMOVED***GetMyNotifications() {
    Authentication authentication = mock(Authentication.class);
    when(authentication.getName()).thenReturn("***REMOVED***User");
    SecurityContextHolder.getContext().setAuthentication(authentication);

    List<Notification> notifications = Arrays.asList(notification);
    when(notificationRepository.findByUser_UserName("***REMOVED***User")).thenReturn(notifications);
    when(notificationMapper.toDTO(any(Notification.class))).thenReturn(notificationDTO);

    List<NotificationDTO> result = notificationService.getMyNotifications();

    assertNotNull(result);
    assertEquals(1, result.size());
    assertEquals("Test Notification", result.get(0).getContent());
  }

  @Test
  void ***REMOVED***UpdateNotification() {
    when(notificationRepository.findById(1L)).thenReturn(Optional.of(notification));
    when(notificationRepository.save(notification)).thenReturn(notification);

    notificationService.update(1L, notificationDTO);

    verify(notificationMapper, times(1)).updateEntity(notification, notificationDTO);
    verify(notificationRepository, times(1)).save(notification);

    assertEquals("Test Notification", notification.getContent());
  }

  @Test
  void ***REMOVED***DeleteNotification() {
    when(notificationRepository.existsById(1L)).thenReturn(true);

    notificationService.delete(1L);

    verify(notificationRepository, times(1)).deleteById(1L);
  }

  @Test
  void ***REMOVED***DeleteNotificationNotFound() {
    when(notificationRepository.existsById(1L)).thenReturn(false);

    Exception exception = assertThrows(AppEx.class, () -> notificationService.delete(1L));
    assertEquals("Notification not found", exception.getMessage());
  }
}
