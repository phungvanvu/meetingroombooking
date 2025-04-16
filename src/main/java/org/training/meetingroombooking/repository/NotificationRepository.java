package org.training.meetingroombooking.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.training.meetingroombooking.entity.models.Notification;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
  List<Notification> findByUser_UserName(String userName);
}
