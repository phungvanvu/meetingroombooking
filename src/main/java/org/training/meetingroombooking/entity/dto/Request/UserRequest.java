package org.training.meetingroombooking.entity.dto.Request;

import jakarta.validation.constraints.*;

import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRequest {

  @NotBlank(message = "Username cannot be blank")
  @NotNull(message = "Username cannot be null")
  @Size(max = 50, message = "Username cannot exceed 50 characters")
  @Pattern(regexp = "^[A-Za-z0-9]+$", message = "Username must contain only non-accented letters and numbers")
  @Pattern(regexp = "^[^\\s]+$", message = "Username and password should not contain any spaces")
  private String userName;

  @NotNull(message = "FullName cannot be null")
  @Size(max = 100, message = "Full name cannot exceed 100 characters")
  private String fullName;

  @NotBlank(message = "Department cannot be blank")
  @NotNull(message = "Department cannot be null")
  @Size(max = 50, message = "department cannot exceed 50 characters")
  private String department;

  @Pattern(regexp = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$",
      message = "Invalid email format. Please provide a valid email address with proper domain (e.g., user@example.com).")
  @Size(max = 100, message = "Email cannot exceed 100 characters")
  @NotNull(message = "Email cannot be null")
  private String email;

  @Size(max = 20, message = "Phone number cannot exceed 20 characters")
  private String phoneNumber;

  @Pattern(regexp = "^[^\\s]+$", message = "Username and password should not contain any spaces")
  @Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
          message = "Password must be at least 8 characters, contain at least one uppercase letter, one lowercase letter, one number, and one special character")
  private String password;

  private boolean enabled = true;

  @NotBlank(message = "Position cannot be blank")
  @NotNull(message = "Position cannot be null")
  private String position;

  @NotBlank(message = "Group cannot be blank")
  @NotNull(message = "Group cannot be null")
  private String group;

  @NotEmpty(message = "User must have role")
  private Set<String> roles;
}
