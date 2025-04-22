package org.training.meetingroombooking.entity.dto.Request;

import jakarta.validation.constraints.*;
import java.util.Set;

import jakarta.validation.groups.Default;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.training.meetingroombooking.validation.impl.ValidationUser;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRequest {

  // userName: Không null, không để trống, tối đa 50 ký tự, chỉ chứa chữ cái và số, không chứa
  // khoảng trắng
  @NotBlank(message = "{username.notblank}", groups = Default.class)
  @Size(max = 50, message = "{username.maxsize}", groups = Default.class)
  @Pattern(regexp = "^[A-Za-z0-9]+$", message = "{username.pattern.letters}", groups = Default.class)
  @Pattern(regexp = "^[^\\s]+$", message = "{username.pattern.nospaces}", groups = Default.class)
  private String userName;

  @NotBlank(message = "{fullname.notblank}", groups = Default.class)
  @Size(max = 100, message = "{fullname.maxsize}", groups = Default.class)
  private String fullName;

  @NotBlank(message = "{department.notblank}", groups = Default.class)
  @Size(max = 50, message = "{department.maxsize}", groups = Default.class)
  private String department;

  // Email: Không được null, tối đa 100 ký tự, phải đúng định dạng email (ví dụ: example@mail.com)
  @NotBlank(message = "{email.notblank}", groups = Default.class)
  @Size(max = 100, message = "{email.maxsize}", groups = Default.class)
  @Pattern(regexp = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$", message = "{email.invalid}", groups = Default.class)
  private String email;

  // phải là số
  @Size(max = 20, message = "{phone.maxsize}", groups = Default.class)
  @Pattern(regexp = "^[0-9]+$", message = "{phone.pattern.digits}", groups = Default.class)
  private String phoneNumber;

  // password: Không để trống, không chứa khoảng trắng, tối thiểu 8 ký tự và phải có:
  // - chữ cái viết hoa
  // - chữ cái viết thường
  // - chữ số
  // - ký tự đặc biệt (@$!%*?&)
  @NotBlank(message = "{password.notblank}", groups = ValidationUser.OnCreate.class)
  @Pattern(regexp = "^[^\\s]+$", message = "{password.pattern.nospaces}", groups = Default.class)
  @Pattern(
          regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
          message = "{password.pattern.complexity}", groups = Default.class)
  private String password;

  @NotNull(message = "{enabled.notnull}", groups = Default.class)
  private boolean enabled;

  @NotBlank(message = "{position.notblank}", groups = Default.class)
  private String position;

  @NotBlank(message = "{group.notblank}", groups = Default.class)
  private String group;

  @NotEmpty(message = "{roles.notempty}", groups = Default.class)
  private Set<String> roles;
}
