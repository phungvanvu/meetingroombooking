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

  @NotBlank(message = "{password.old.notblank}")
  @Pattern(regexp = "^[^\\s]+$", message = "{password.pattern.nospaces}")
  @Pattern(
      regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
      message = "{password.pattern.complexity}")
  private String oldPassword;

  @NotBlank(message = "{password.new.notblank}")
  @Pattern(regexp = "^[^\\s]+$", message = "{password.pattern.nospaces}")
  @Pattern(
      regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
      message = "{password.pattern.complexity}")
  private String newPassword;
}
