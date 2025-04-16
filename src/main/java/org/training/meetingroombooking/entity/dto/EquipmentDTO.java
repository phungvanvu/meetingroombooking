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
public class EquipmentDTO {

  @NotBlank(message = "{equipment.name.notblank}")
  @Size(max = 50, message = "{equipment.name.maxsize}")
  private String equipmentName;

  @Size(max = 255, message = "{equipment.description.maxsize}")
  private String description;
}
