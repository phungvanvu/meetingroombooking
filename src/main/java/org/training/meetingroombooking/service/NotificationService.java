package org.training.meetingroombooking.service;

import java.util.List;
import org.training.meetingroombooking.entity.dto.NotificationDTO;

public interface NotificationService {
  NotificationDTO create(NotificationDTO dto);

  List<NotificationDTO> getAll();

  List<NotificationDTO> getNotificationsByUserName(String userName);

  List<NotificationDTO> getMyNotifications();

  NotificationDTO update(Long id, NotificationDTO dto);

  void delete(Long id);
}
