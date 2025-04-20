package org.training.meetingroombooking.entity.dto.Request;

import jakarta.validation.constraints.*;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRequest {

  // userName: Không null, không để trống, tối đa 50 ký tự, chỉ chứa chữ cái và số, không chứa khoảng trắng
  @NotBlank(message = "{username.notblank}")
  @NotNull(message = "{username.notnull}")
  @Size(max = 50, message = "{username.maxsize}")
  @Pattern(regexp = "^[A-Za-z0-9]+$", message = "{username.pattern.letters}")
  @Pattern(regexp = "^[^\\s]+$", message = "{username.pattern.nospaces}")
  private String userName;

  @NotNull(message = "{fullname.notnull}")
  @Size(max = 100, message = "{fullname.maxsize}")
  private String fullName;

  @NotBlank(message = "{department.notblank}")
  @NotNull(message = "{department.notnull}")
  @Size(max = 50, message = "{department.maxsize}")
  private String department;

  // Email: Không được null, tối đa 100 ký tự, phải đúng định dạng email (ví dụ: example@mail.com)
  @Pattern(regexp = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$", message = "{email.invalid}")
  @Size(max = 100, message = "{email.maxsize}")
  @NotNull(message = "{email.notnull}")
  private String email;

  // phải là số
  @Size(max = 20, message = "{phone.maxsize}")
  @Pattern(regexp = "^[0-9]+$", message = "{phone.pattern.digits}")
  private String phoneNumber;

  // password: Không để trống, không chứa khoảng trắng, tối thiểu 8 ký tự và phải có:
  // - chữ cái viết hoa
  // - chữ cái viết thường
  // - chữ số
  // - ký tự đặc biệt (@$!%*?&)
  @Pattern(regexp = "^[^\\s]+$", message = "{password.pattern.nospaces}")
  @Pattern(
      regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
      message = "{password.pattern.complexity}")
  private String password;

  @NotNull(message = "{enabled.notnull}")
  private boolean enabled;

  @NotBlank(message = "{position.notblank}")
  @NotNull(message = "{position.notnull}")
  private String position;

  @NotBlank(message = "{group.notblank}")
  @NotNull(message = "{group.notnull}")
  private String group;

  @NotEmpty(message = "{roles.notempty}")
  private Set<String> roles;
}
