package org.training.meetingroombooking.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.training.meetingroombooking.dto.AuthRequest;
import org.training.meetingroombooking.repository.UserRepository;

@Service
public class AuthService {
    UserRepository userRepository;

    public boolean authenticate(AuthRequest request) {
        var user =  userRepository.findByUserName(request.getUsername())
                .orElseThrow(()-> new RuntimeException("user not existed"));
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        return passwordEncoder.matches(request.getPassword(), user.getPassword());
    }

}
