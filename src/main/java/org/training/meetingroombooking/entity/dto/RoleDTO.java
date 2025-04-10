package org.training.meetingroombooking.entity.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoleDTO {

    @NotBlank(message = "Role name cannot be blank")
    @Size(max = 50, message = "Role names cannot exceed 50 characters.")
    private String roleName;

    @Size(max = 255, message = "Description must not exceed 255 characters")
    private String description;

    private Set<String> permissions;
}
