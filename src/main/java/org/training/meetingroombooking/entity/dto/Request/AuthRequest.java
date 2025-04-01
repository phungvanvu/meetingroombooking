package org.training.meetingroombooking.entity.dto.Request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthRequest {
    @NotBlank(message = "Username cannot be left blank")
    @NotNull(message = "Username cannot be null")
    @Size(max = 50, message = "Username cannot exceed 50 characters")
    @Pattern(regexp = "^[A-Za-z0-9]+$", message = "Username must contain only non-accented letters and numbers")
    @Pattern(regexp = "^[^\\s]+$", message = "Username and password should not contain any spaces")
    private String username;

    @NotBlank(message = "Password cannot be left blank")
    @Size(min = 8, max = 50, message = "Password must be between 8 and 50 characters")
    @Pattern(regexp = "^[^\\s]+$", message = "Username and password should not contain any spaces")
    private String password;

}
