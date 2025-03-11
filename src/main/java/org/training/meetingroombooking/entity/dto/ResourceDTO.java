package org.training.meetingroombooking.entity.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResourceDTO {

    @NotBlank(message = "Name cannot be left blank")
    @Size(max = 75, message = "Name cannot exceed 75 characters")
    private String name;

    @Pattern(regexp = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$",
            message = "Invalid email format. Please provide a valid email address with proper domain (e.g., user@example.com).")
    @Size(max = 100, message = "Email cannot exceed 100 characters")
    private String email;

    @Pattern(
            regexp = "^\\+?[0-9. ()-]{7,15}$",
            message = "Invalid phone number"
    )
    @Size(max = 20, message = "Phone number cannot exceed 20 characters")
    private String phone;

    @Size(max = 50, message = "The processing procedure must not exceed 50 characters.")
    private String stepProcess;

    @NotNull(message = "Creator cannot be null")
    private Long createdById;

    private Long assigneeId;

    @Size(max = 100, message = "Action cannot exceed 100 characters")
    private String action;
}