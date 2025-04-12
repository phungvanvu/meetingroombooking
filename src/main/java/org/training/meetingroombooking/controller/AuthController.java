package org.training.meetingroombooking.controller;

import com.nimbusds.jose.JOSEException;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.web.bind.annotation.*;
import org.training.meetingroombooking.entity.dto.Request.LogoutRequest;
import org.training.meetingroombooking.entity.dto.Request.RefreshRequest;
import org.training.meetingroombooking.entity.dto.Response.ApiResponse;
import org.training.meetingroombooking.entity.dto.Request.AuthRequest;
import org.training.meetingroombooking.entity.dto.Response.AuthResponse;
import org.training.meetingroombooking.entity.dto.Request.IntrospectRequest;
import org.training.meetingroombooking.entity.dto.Response.IntrospectResponse;
import org.training.meetingroombooking.service.AuthService;
import org.training.meetingroombooking.service.PasswordResetService;

import java.text.ParseException;

@RestController
@RequestMapping("/auth")
public class AuthController {

  private final AuthService authService;
  private final PasswordResetService passwordResetService;

  public AuthController(AuthService authService, PasswordResetService passwordResetService) {
    this.authService = authService;
    this.passwordResetService = passwordResetService;
  }

  @PostMapping("/login")
  ApiResponse<AuthResponse> auth(@Valid @RequestBody AuthRequest request) {
    var result = authService.authenticate(request);
    return ApiResponse.<AuthResponse>builder()
        .success(true)
        .data(AuthResponse.builder()
            .accessToken(result.getAccessToken())
            .refreshToken(result.getRefreshToken())
            .build())
        .build();
  }

  @PostMapping("/introspect")
  ApiResponse<IntrospectResponse> auth(@RequestBody IntrospectRequest request)
      throws ParseException, JOSEException {
    var result = authService.introspect(request);

    return ApiResponse.<IntrospectResponse>builder()
        .success(true)
        .data(result)
        .build();

  }

  @PostMapping("/logout")
  ApiResponse<Void> logout(@RequestBody LogoutRequest request)
      throws ParseException, JOSEException {
    authService.logout(request);
    return ApiResponse.<Void>builder()
        .success(true)
        .build();
  }

  @PostMapping("/refresh")
  ApiResponse<AuthResponse> authenticate(@RequestBody RefreshRequest request)
      throws ParseException, JOSEException {
    var result = authService.refreshToken(request);
    return ApiResponse.<AuthResponse>builder()
        .success(true)
        .data(result)
        .build();
  }

  /**
   * Endpoint gửi OTP đến email của người dùng để reset mật khẩu.
   *
   * @param email Email của người dùng
   * @return ApiResponse thông báo thành công hay thất bại.
   */
  @PostMapping("/forgot-password")
  public ApiResponse<String> forgotPassword(@RequestParam("email") @NotBlank String email) {
    try {
      // Lấy thông báo chung từ service
      String message = passwordResetService.initiatePasswordReset(email);
      return ApiResponse.<String>builder()
              .success(true)
              .data(message)
              .build();
    } catch (MessagingException e) {
      return ApiResponse.<String>builder()
              .success(false)
              .data("OTP sending failed: " + e.getMessage())
              .build();
    }
  }

  /**
   * Endpoint reset mật khẩu sau khi người dùng xác thực OTP.
   *
   * @param email       Email của người dùng
   * @param otp         Mã OTP nhận được
   * @param newPassword Mật khẩu mới
   * @return ApiResponse thông báo kết quả cập nhật mật khẩu.
   */
  @PostMapping("/reset-password")
  public ApiResponse<String> resetPassword(@RequestParam("email") @NotBlank String email,
                                           @RequestParam("otp") @NotBlank String otp,
                                           @RequestParam("newPassword") @NotBlank String newPassword) {
    passwordResetService.resetPassword(email, otp, newPassword);
    return ApiResponse.<String>builder()
            .success(true)
            .data("Password updated successfully.")
            .build();
  }
}
