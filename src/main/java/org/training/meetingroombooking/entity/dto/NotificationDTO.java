package org.training.meetingroombooking.entity.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.training.meetingroombooking.entity.enums.NotificationType;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationDTO {

  @NotBlank(message = "Notification content cannot be blank")
  @Size(max = 255, message = "Notification content name cannot exceed 255 characters")
  private String content;

  @NotNull(message = "Notification type cannot be null")
  private NotificationType type;

  @NotNull(message = "Notification recipient ID cannot be null")
  private Long userId;

  private LocalDateTime createdAt;
}

