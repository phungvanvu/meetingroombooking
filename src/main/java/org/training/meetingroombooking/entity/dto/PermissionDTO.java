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

    @NotBlank(message = "permissionName cannot be blank")
    @Size(max = 50, message = "permissionName cannot exceed 50 characters")
    private String permissionName;

    @Size(max = 255, message = "description cannot exceed 255 characters")
    private String description;

}
