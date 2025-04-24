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

  // oldPassword: Không để trống, không chứa khoảng trắng, tối thiểu 8 ký tự và phải có:
  // - chữ cái viết hoa
  // - chữ cái viết thường
  // - chữ số
  // - ký tự đặc biệt (@$!%*?&)
  @NotBlank(message = "{password.old.notblank}")
  private String oldPassword;

  // newPassword: Không để trống, không chứa khoảng trắng, tối thiểu 8 ký tự và phải có:
  // - chữ cái viết hoa
  // - chữ cái viết thường
  // - chữ số
  // - ký tự đặc biệt (@$!%*?&)
  @NotBlank(message = "{password.new.notblank}")
  @Pattern(regexp = "^[^\\s]+$", message = "{password.pattern.nospaces}")
  @Pattern(
      regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
      message = "{password.pattern.complexity}")
  private String newPassword;
}
