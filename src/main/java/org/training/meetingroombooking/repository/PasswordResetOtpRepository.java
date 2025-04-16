package org.training.meetingroombooking.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.training.meetingroombooking.entity.models.PasswordResetOtp;

public interface PasswordResetOtpRepository extends JpaRepository<PasswordResetOtp, String> {
  Optional<PasswordResetOtp> findByEmailAndOtp(String email, String otp);
}
