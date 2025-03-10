package org.training.meetingroombooking.entity.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class PermissionDTO {

    @NotBlank(message = "namePermission cannot be blank")
    @Size(min = 3, max = 50, message = "namePermission must be between 3 and 50 characters")
    private String namePermission;

    @Size(max = 255, message = "description cannot exceed 255 characters")
    private String description;

    public PermissionDTO() {}

    public PermissionDTO(String namePermission, String description) {
        this.namePermission = namePermission;
        this.description = description;
    }

    public String getNamePermission() {
        return namePermission;
    }

    public void setNamePermission(String namePermission) {
        this.namePermission = namePermission;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public static class Builder {
        private String namePermission;
        private String description;

        public Builder() {}

        public Builder namePermission(String namePermission) {
            this.namePermission = namePermission;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public PermissionDTO build() {
            return new PermissionDTO(namePermission, description);
        }
    }
}
