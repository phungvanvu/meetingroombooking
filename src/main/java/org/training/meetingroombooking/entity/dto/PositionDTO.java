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
public class PositionDTO {

  @NotBlank(message = "Position name cannot be blank")
  @Size(max = 75, message = "Position name cannot exceed 75 characters")
  private String positionName;

  @Size(max = 255, message = "Description must not exceed 255 characters")
  private String description;
}
