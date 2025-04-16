package org.training.meetingroombooking.entity.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PermissionDTO {

  @NotBlank(message = "{permission.name.notblank}")
  @Size(max = 50, message = "{permission.name.maxsize}")
  private String permissionName;

  @Size(max = 255, message = "{permission.description.maxsize}")
  private String description;
}
