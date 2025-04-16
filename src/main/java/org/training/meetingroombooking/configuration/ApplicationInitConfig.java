package org.training.meetingroombooking.configuration;

import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.training.meetingroombooking.entity.models.Role;
import org.training.meetingroombooking.entity.models.User;
import org.training.meetingroombooking.repository.RoleRepository;
import org.training.meetingroombooking.repository.UserRepository;

@Configuration
@Slf4j
public class ApplicationInitConfig {

  @Value("${app.admin.username}")
  private String adminUsername;

  @Value("${app.admin.fullname}")
  private String adminFullName;

  @Value("${app.admin.password}")
  private String adminPassword;

  @Value("${app.admin.email}")
  private String adminEmail;

  @Value("${app.admin.phone}")
  private String adminPhone;

  @Value("${app.admin.department}")
  private String adminDepartment;

  @Value("${app.admin.role}")
  private String adminRoleName;

  private final PasswordEncoder passwordEncoder;
  private final UserRepository userRepository;
  private final RoleRepository roleRepository;

  public ApplicationInitConfig(
      PasswordEncoder passwordEncoder,
      UserRepository userRepository,
      RoleRepository roleRepository) {
    this.passwordEncoder = passwordEncoder;
    this.userRepository = userRepository;
    this.roleRepository = roleRepository;
  }

  @Bean
  ApplicationRunner applicationRunner() {
    return args -> {
      Role adminRole =
          roleRepository
              .findById(adminRoleName)
              .orElseGet(
                  () -> {
                    Role newRole = new Role();
                    newRole.setRoleName(adminRoleName);
                    newRole.setDescription("Administrator role with full access");
                    return roleRepository.save(newRole);
                  });

      if (userRepository.findByUserName(adminUsername).isEmpty()) {
        User user = new User();
        user.setUserName(adminUsername);
        user.setFullName(adminFullName);
        user.setDepartment(adminDepartment);
        user.setPhoneNumber(adminPhone);
        user.setEmail(adminEmail);
        user.setPassword(passwordEncoder.encode(adminPassword));
        user.setEnabled(true);
        user.setRoles(Set.of(adminRole));
        userRepository.save(user);
        log.warn("Admin account has been created. Please change the default password.");
      }
    };
  }
}
