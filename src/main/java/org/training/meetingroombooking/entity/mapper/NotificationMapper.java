package org.training.meetingroombooking.entity.mapper;

import org.training.meetingroombooking.entity.dto.NotificationDTO;
import org.training.meetingroombooking.entity.models.Notification;

public interface NotificationMapper {
  Notification toEntity(NotificationDTO dto);
  NotificationDTO toDTO(Notification entity);
}
