package org.training.meetingroombooking.entity.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.Set;

public class RoleDTO {

    @NotBlank(message = "roleName cannot be blank")
    @Size(min = 3, max = 50, message = "namePermission must be between 3 and 50 characters")
    private String roleName;

    @Size(max = 255, message = "description cannot exceed 255 characters")
    private String description;

    private Set<String> permissions;

    public RoleDTO() {}

    public RoleDTO(String roleName, String description, Set<String> permissions) {
        this.roleName = roleName;
        this.description = description;
        this.permissions = permissions;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<String> getPermissions() {
        return permissions;
    }

    public void setPermissions(Set<String> permissions) {
        this.permissions = permissions;
    }
}
