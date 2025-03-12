package org.training.meetingroombooking.entity.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.training.meetingroombooking.entity.dto.NotificationDTO;
import org.training.meetingroombooking.entity.models.Notification;
import org.training.meetingroombooking.repository.UserRepository;

@Component
public class NotificationMapperImpl implements NotificationMapper {

  @Autowired
  private UserRepository userRepository;

  @Override
  public Notification toEntity(NotificationDTO dto) {
    if (dto == null) {
      return null;
    }
    Notification entity = new Notification();
    entity.setContent(dto.getContent());
    entity.setCreatedAt(dto.getCreatedAt());
    entity.setType(dto.getType());
    entity.setRead(dto.getIsRead());
    if (dto.getUserId() != null) {
      entity.setUser(userRepository.findById(dto.getUserId()).orElse(null));
    }
    return entity;
  }

  @Override
  public NotificationDTO toDTO(Notification entity) {
    if (entity == null) {
      return null;
    }
    return NotificationDTO.builder()
        .content(entity.getContent())
        .type(entity.getType())
        .createdAt(entity.getCreatedAt())
        .isRead(entity.isRead())
        .userId(entity.getUser() != null ? entity.getUser().getUserId() : null)
        .build();
  }
}
