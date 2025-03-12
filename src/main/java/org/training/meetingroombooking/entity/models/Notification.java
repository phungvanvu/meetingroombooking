package org.training.meetingroombooking.entity.models;

import jakarta.persistence.*;
import org.training.meetingroombooking.entity.enums.NotificationType;

import java.time.LocalDateTime;

@Entity
@Table(name = "notifications")
public class Notification {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int notificationId;

  private String content;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private NotificationType type; // Loại thông báo (INFO, WARNING, ERROR)

  private boolean isRead;

  @ManyToOne
  @JoinColumn(name = "user_id")
  private User user; // Người nhận thông báo

  @Column(name = "created_at")
  @Temporal(TemporalType.TIMESTAMP)
  private LocalDateTime createdAt;

  @PrePersist
  protected void onCreate() {
    this.createdAt = LocalDateTime.now();
  }
}
