package org.training.meetingroombooking.entity.dto.Request;

import jakarta.validation.constraints.NotBlank;
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
  private String username;

  @NotBlank(message = "{password.notblank}")
  private String password;
}
