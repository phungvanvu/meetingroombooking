package org.training.meetingroombooking.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.training.meetingroombooking.entity.dto.NotificationDTO;
import org.training.meetingroombooking.entity.mapper.NotificationMapper;
import org.training.meetingroombooking.entity.models.Notification;
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

    // ✅ Lấy danh sách thông báo theo userId
    public List<NotificationDTO> getNotificationsByUserId(Long userId) {
        List<Notification> notifications = notificationRepository.findByUserUserId(userId);
        return notifications.stream()
                .map(notificationMapper::toDTO)
                .collect(Collectors.toList());
    }


//    public NotificationDTO updateNotification(Long id, NotificationDTO dto) {
//        Notification existingNotification = notificationRepository.findById(id)
//                .orElseThrow(() -> new EntityNotFoundException("Notification not found with id: " + id));
//
//        // Ánh xạ dữ liệu từ DTO vào entity hiện tại
//        notificationMapper.updateEntity(existingNotification, dto);
//
//        existingNotification = notificationRepository.save(existingNotification);
//        return notificationMapper.toDTO(existingNotification);
//    }


    public void deleteNotification(Long id) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Notification not found with id: " + id));
        notificationRepository.delete(notification);
    }
}
