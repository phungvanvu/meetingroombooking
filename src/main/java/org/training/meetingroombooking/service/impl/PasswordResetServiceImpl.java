package org.training.meetingroombooking.service.impl;

import jakarta.mail.MessagingException;
import java.time.LocalDateTime;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.training.meetingroombooking.entity.enums.ErrorCode;
import org.training.meetingroombooking.entity.models.PasswordResetOtp;
import org.training.meetingroombooking.entity.models.User;
import org.training.meetingroombooking.exception.AppEx;
import org.training.meetingroombooking.repository.PasswordResetOtpRepository;
import org.training.meetingroombooking.repository.UserRepository;
import org.training.meetingroombooking.service.EmailService;
import org.training.meetingroombooking.service.PasswordResetService;

@Slf4j
@Service
public class PasswordResetServiceImpl implements PasswordResetService {

  private final UserRepository userRepository;
  private final PasswordResetOtpRepository otpRepository;
  private final EmailService emailService;
  private final PasswordEncoder passwordEncoder;
  private final TemplateEngine templateEngine;

  public PasswordResetServiceImpl(
      UserRepository userRepository,
      PasswordResetOtpRepository otpRepository,
      EmailService emailService,
      TemplateEngine templateEngine) {
    this.userRepository = userRepository;
    this.otpRepository = otpRepository;
    this.emailService = emailService;
    this.templateEngine = templateEngine;
    this.passwordEncoder = new BCryptPasswordEncoder(10);
  }

  @Override
  public String initiatePasswordReset(String email) throws MessagingException {
    Optional<User> optionalUser = userRepository.findByEmail(email);
    if (optionalUser.isPresent()) {
      User user = optionalUser.get();
      String otpCode = generateOTP();
      PasswordResetOtp otp = new PasswordResetOtp();
      otp.setOtp(otpCode);
      otp.setEmail(email);
      otp.setExpiryDate(LocalDateTime.now().plusMinutes(5));
      otpRepository.save(otp);
      Context context = new Context();
      context.setVariable("fullName", user.getFullName());
      context.setVariable("otpCode", otpCode);
      String htmlBody = templateEngine.process("password-reset", context);
      emailService.sendHtmlEmail(email, "Password Reset OTP", htmlBody);
    }
    return "If an account with that email exists, an OTP has been sent to your email address.";
  }


  @Override
  public void resetPassword(String email, String otpCode, String newPassword) {
    PasswordResetOtp passwordResetOtp =
        otpRepository
            .findByEmailAndOtp(email, otpCode)
            .orElseThrow(() -> new AppEx(ErrorCode.INVALID_OTP));
    if (passwordResetOtp.getExpiryDate().isBefore(LocalDateTime.now())) {
      otpRepository.delete(passwordResetOtp);
      throw new AppEx(ErrorCode.OTP_EXPIRED);
    }
    User user =
        userRepository.findByEmail(email).orElseThrow(() -> new AppEx(ErrorCode.USER_NOT_FOUND));
    user.setPassword(passwordEncoder.encode(newPassword));
    userRepository.save(user);
    otpRepository.delete(passwordResetOtp);
  }

  private String generateOTP() {
    int randomPIN = (int) (Math.random() * 900000) + 100000;
    return String.valueOf(randomPIN);
  }
}
