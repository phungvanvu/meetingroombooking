package org.training.meetingroombooking.entity.dto.Request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.training.meetingroombooking.entity.models.GroupEntity;
import org.training.meetingroombooking.entity.models.Position;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRequest {

  @NotBlank(message = "Username cannot be left blank")
  @Size(max = 50, message = "UserName cannot exceed 50 characters")
  @Pattern(regexp = "^[^\\s]+$", message = "Username and password should not contain any spaces")
  private String userName;

  @NotNull(message = "FullName cannot be null")
  @Size(max = 100, message = "Full name cannot exceed 100 characters")
  private String fullName;

  @Size(max = 50, message = "department cannot exceed 50 characters")
  private String department;

  @Pattern(regexp = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$",
      message = "Invalid email format. Please provide a valid email address with proper domain (e.g., user@example.com).")
  @Size(max = 100, message = "Email cannot exceed 100 characters")
  private String email;

  @Size(max = 20, message = "Phone number cannot exceed 20 characters")
  private String phoneNumber;

  @Size(min = 8, max = 50, message = "Password must be between 8 and 50 characters")
  @Pattern(regexp = "^[^\\s]+$", message = "Username and password should not contain any spaces")
  private String password;

  private boolean enabled = true;

  private Position position;

  private GroupEntity group;

  private Set<String> roles;
}
