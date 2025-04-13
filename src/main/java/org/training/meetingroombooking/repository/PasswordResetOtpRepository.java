package org.training.meetingroombooking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.training.meetingroombooking.entity.models.PasswordResetOtp;
import java.util.Optional;

public interface PasswordResetOtpRepository extends JpaRepository<PasswordResetOtp, String> {
    Optional<PasswordResetOtp> findByEmailAndOtp(String email, String otp);
}