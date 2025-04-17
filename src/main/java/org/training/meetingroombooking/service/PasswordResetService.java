package org.training.meetingroombooking.service;

import jakarta.mail.MessagingException;

public interface PasswordResetService {
  String initiatePasswordReset(String email) throws MessagingException;

  void resetPassword(String email, String otpCode, String newPassword);
}
