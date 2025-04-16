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

  @NotBlank(message = "{position.notblank}")
  @Size(max = 75, message = "{position.name.maxsize}")
  private String positionName;

  @Size(max = 255, message = "{position.description.maxsize}")
  private String description;
}
