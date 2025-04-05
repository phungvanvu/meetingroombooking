package org.training.meetingroombooking.entity.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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

    @NotBlank(message = "Device name cannot be Null")
    @Size(max = 50, message = "Device name cannot exceed 50 characters")
    private String equipmentName;

    @Size(max = 255, message = "Device description cannot exceed 255 characters")
    private String description;

    @NotNull(message = "Device status cannot be null")
    private Boolean available;
}
