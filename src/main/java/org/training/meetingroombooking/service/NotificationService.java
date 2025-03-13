package org.training.meetingroombooking.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.training.meetingroombooking.entity.dto.NotificationDTO;
import org.training.meetingroombooking.entity.enums.ErrorCode;
import org.training.meetingroombooking.entity.mapper.NotificationMapper;
import org.training.meetingroombooking.entity.models.Notification;
import org.training.meetingroombooking.exception.AppEx;
import org.training.meetingroombooking.repository.NotificationRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final NotificationMapper notificationMapper;

    public NotificationDTO createNotification(NotificationDTO dto) {
        Notification notification = notificationMapper.toEntity(dto);
        notification = notificationRepository.save(notification);
        return notificationMapper.toDTO(notification);
    }

    public List<NotificationDTO> getNotificationsByUserId(Long userId) {
        List<Notification> notifications = notificationRepository.findByUserUserId(userId);
        return notifications.stream()
                .map(notificationMapper::toDTO)
                .collect(Collectors.toList());
    }

    public void deleteNotification(Long id) {
        if (!notificationRepository.existsById(id)) {
            throw new AppEx(ErrorCode.NOTIFICATION_NOT_FOUND);
        }
        notificationRepository.deleteById(id);
    }
}
