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

    @NotBlank(message = "namePermission cannot be blank")
    @Size(max = 50, message = "namePermission cannot exceed 50 characters")
    private String namePermission;

    @Size(max = 255, message = "description cannot exceed 255 characters")
    private String description;

}
