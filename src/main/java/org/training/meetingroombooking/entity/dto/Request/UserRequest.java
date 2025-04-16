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

  @Pattern(regexp = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$", message = "{email.invalid}")
  @Size(max = 100, message = "{email.maxsize}")
  @NotNull(message = "{email.notnull}")
  private String email;

  @Size(max = 20, message = "{phone.maxsize}")
  private String phoneNumber;

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
