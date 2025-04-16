package org.training.meetingroombooking.entity.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
public class NotificationDTO {

  @NotBlank(message = "{notification.content.notblank}")
  @Size(max = 255, message = "{notification.content.maxsize}")
  private String content;

  @NotNull(message = "{notification.type.notnull}")
  private NotificationType type;

  @NotNull(message = "{notification.read.notnull}")
  private Boolean hasRead;

  @NotNull(message = "{notification.userId.notnull}")
  private Long userId;

  private LocalDateTime createdAt;
}
