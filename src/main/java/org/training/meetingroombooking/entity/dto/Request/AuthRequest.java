package org.training.meetingroombooking.entity.dto.Request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthRequest {

  @NotBlank(message = "{username.notblank}")
  @NotNull(message = "{username.notnull}")
  @Size(max = 50, message = "{username.maxsize}")
  @Pattern(regexp = "^[A-Za-z0-9]+$", message = "{username.pattern.letters}")
  @Pattern(regexp = "^[^\\s]+$", message = "{username.pattern.nospaces}")
  private String username;

  @NotBlank(message = "{password.notblank}")
  @Size(min = 8, max = 50, message = "{password.size}")
  @Pattern(regexp = "^[^\\s]+$", message = "{password.pattern.nospaces}")
  private String password;
}
