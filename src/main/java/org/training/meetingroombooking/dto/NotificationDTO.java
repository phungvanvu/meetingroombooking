package org.training.meetingroombooking.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.training.meetingroombooking.entity.User;

@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString
public class NotificationDTO {
  @Size(min = 1, max = 255, message = "message must be between 1 and 255 characters")
  private String message;
  @NotNull
  private User user;
}
