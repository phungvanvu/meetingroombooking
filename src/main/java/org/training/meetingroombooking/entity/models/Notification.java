package org.training.meetingroombooking.entity.models;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.training.meetingroombooking.entity.enums.NotificationType;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "notifications")
public class Notification {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long notificationId;

  @Column(length = 255)
  private String content;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private NotificationType type; // Loại thông báo (INFO, WARNING, ERROR)

  @Column(nullable = false)
  private Boolean hasRead;

  @ManyToOne
  @JoinColumn(name = "user_id")
  private User user; // Người nhận thông báo

  @Column(name = "created_at")
  @Temporal(TemporalType.TIMESTAMP)
  private LocalDateTime createdAt;

  @PrePersist
  protected void onCreate() {
    if (this.hasRead == null) {
      this.hasRead = false;
    }
    this.createdAt = LocalDateTime.now();
  }
}
