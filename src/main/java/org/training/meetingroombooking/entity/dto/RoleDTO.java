package org.training.meetingroombooking.entity.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoleDTO {

  @NotBlank(message = "{role.name.notblank}")
  @Size(max = 50, message = "{role.name.maxsize}")
  private String roleName;

  @Size(max = 255, message = "{role.description.maxsize}")
  private String description;

  private Set<String> permissions;
}
