package org.training.meetingroombooking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.training.meetingroombooking.entity.models.Notification;

public interface NotificationRepository extends JpaRepository<Notification,Long> {

}
