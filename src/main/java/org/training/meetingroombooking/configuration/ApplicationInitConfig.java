package org.training.meetingroombooking.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.training.meetingroombooking.entity.models.Role;
import org.training.meetingroombooking.entity.models.User;
import org.training.meetingroombooking.repository.RoleRepository;
import org.training.meetingroombooking.repository.UserRepository;

import java.util.Set;

@Slf4j
@Configuration
public class ApplicationInitConfig {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public ApplicationInitConfig(PasswordEncoder passwordEncoder, UserRepository userRepository, RoleRepository roleRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Bean
    ApplicationRunner applicationRunner() {
        return args -> {
            Role adminRole = roleRepository.findById("ADMIN").orElseGet(() -> {
                Role newRole = new Role();
                newRole.setRoleName("ADMIN");
                newRole.setDescription("Administrator role with full access");
                return roleRepository.save(newRole);
            });
            if (userRepository.findByUserName("admin").isEmpty()) {
                User user = new User();
                user.setUserName("admin");
                user.setFullName("Admin");
                user.setDepartment("Software Development");
                user.setPhoneNumber("0332182284");
                user.setEmail("phungvanvu0@gmail.com");
                user.setPassword(passwordEncoder.encode("***REMOVED***"));
                user.setEnabled(true);
                user.setRoles(Set.of(adminRole));
                userRepository.save(user);
                log.warn("Admin has been created with default password, please change password");
            }
        };
    }
}
