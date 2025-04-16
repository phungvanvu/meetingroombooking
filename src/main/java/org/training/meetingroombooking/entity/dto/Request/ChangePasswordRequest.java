package org.training.meetingroombooking.entity.dto.Request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChangePasswordRequest {
  @Pattern(regexp = "^[^\\s]+$", message = "Username and password should not contain any spaces")
  @Pattern(
      regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
      message =
          "Password must be at least 8 characters, contain at least one uppercase letter, one lowercase letter, one number, and one special character")
  @NotBlank(message = "Old password must not be blank")
  private String oldPassword;

  @Pattern(regexp = "^[^\\s]+$", message = "Username and password should not contain any spaces")
  @Pattern(
      regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
      message =
          "Password must be at least 8 characters, contain at least one uppercase letter, one lowercase letter, one number, and one special character")
  @NotBlank(message = "New password must not be blank")
  private String newPassword;
}
