package org.training.meetingroombooking.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.training.meetingroombooking.entity.dto.NotificationDTO;
import org.training.meetingroombooking.entity.enums.ErrorCode;
import org.training.meetingroombooking.entity.mapper.NotificationMapper;
import org.training.meetingroombooking.entity.models.Notification;
import org.training.meetingroombooking.exception.AppEx;
import org.training.meetingroombooking.repository.NotificationRepository;

import java.nio.file.attribute.UserPrincipal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final NotificationMapper notificationMapper;

    public NotificationService(NotificationRepository notificationRepository, NotificationMapper notificationMapper) {
        this.notificationRepository = notificationRepository;
        this.notificationMapper = notificationMapper;
    }

    public NotificationDTO create(NotificationDTO dto) {
        Notification notification = notificationMapper.toEntity(dto);
        Notification savedNotification = notificationRepository.save(notification);
        return notificationMapper.toDTO(savedNotification);
    }
    public List<NotificationDTO> getAll() {
        List<Notification> notifications = notificationRepository.findAll();
        return notifications.stream()
            .map(notificationMapper::toDTO)
            .collect(Collectors.toList());
    }

    public List<NotificationDTO> getNotificationsByUserName(String userName) {
        List<Notification> notifications = notificationRepository.findByUser_UserName(userName);
        return notifications.stream()
                .map(notificationMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<NotificationDTO> getMyNotifications() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        List<Notification> notifications = notificationRepository.findByUser_UserName(username);
        return notifications.stream()
                .map(notificationMapper::toDTO)
                .collect(Collectors.toList());
    }

    public NotificationDTO update(Long id, NotificationDTO dto) {
        Notification existingNotification = notificationRepository.findById(id)
                .orElseThrow(() -> new AppEx(ErrorCode.NOTIFICATION_NOT_FOUND));
        notificationMapper.updateEntity(existingNotification, dto);
        Notification updatedNotification = notificationRepository.save(existingNotification);
        return notificationMapper.toDTO(updatedNotification);
    }



    public void delete(Long id) {
        if (!notificationRepository.existsById(id)) {
            throw new AppEx(ErrorCode.NOTIFICATION_NOT_FOUND);
        }
        notificationRepository.deleteById(id);
    }
}
