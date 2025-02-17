package org.training.meetingroombooking.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.training.meetingroombooking.dto.UserDTO;
import org.training.meetingroombooking.entity.Role;
import org.training.meetingroombooking.entity.User;
import org.training.meetingroombooking.exception.AppEx;
import org.training.meetingroombooking.exception.ErrorCode;
import org.training.meetingroombooking.mapper.UserMapper;
import org.training.meetingroombooking.repository.RoleRepository;
import org.training.meetingroombooking.repository.UserRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
public class UserService {
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, RoleRepository roleRepository,
                       UserMapper userMapper) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = new BCryptPasswordEncoder(10);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public UserDTO createUser(UserDTO userDTO) {
        if (userRepository.existsByUserName(userDTO.getUserName())) {
            throw new AppEx(ErrorCode.USER_ALREADY_EXISTS);
        }

        // Kiểm tra email hợp lệ
        if (userDTO.getEmail() != null && !isValidEmailDomain(userDTO.getEmail())) {
            throw new AppEx(ErrorCode.INVALID_EMAIL_DOMAIN);
        }

        // Kiểm tra nếu mật khẩu là null
        if (userDTO.getPassword() == null || userDTO.getPassword().isEmpty()) {
            throw new AppEx(ErrorCode.PASSWORD_NOT_PROVIDED);  // Thêm mã lỗi nếu không có mật khẩu
        }

        User user = userMapper.toEntity(userDTO);
        user.setPassword(passwordEncoder.encode(userDTO.getPassword())); // Mã hóa mật khẩu

        Set<Role> roles = new HashSet<>(roleRepository.findByRoleNameIn(userDTO.getRoles()));
        if (roles.isEmpty()) {
            throw new AppEx(ErrorCode.ROLE_NOT_FOUND);
        }

        user.setRoles(roles);
        user = userRepository.save(user);

        // Ẩn mật khẩu trước khi trả về DTO
        UserDTO responseDTO = userMapper.toDTO(user);
        responseDTO.setPassword(null);  // Đảm bảo không trả về mật khẩu

        return userMapper.toDTO(user);
    }





    public List<UserDTO> getAllUsers() {
        log.info("In method getAllUsers");
        return userRepository.findAll().stream().map(userMapper::toDTO).toList();
    }


    public UserDTO getUserById(int userId) {
        log.info("In method getUserById");
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppEx(ErrorCode.RESOURCE_NOT_FOUND));
        return userMapper.toDTO(user);
    }

    public UserDTO getMyInfo() {
        var context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();

        User user = userRepository.findByUserName(name).orElseThrow(
                () -> new AppEx(ErrorCode.USER_NOT_FOUND));
        return userMapper.toDTO(user);
    }

    // valid tên miền email
    private boolean isValidEmailDomain(String email) {
        return email.matches("^[\\w-\\.]+@[\\w-]+\\.(vn|edu|com)$");
    }

    @PostAuthorize("returnObject.userName == authentication.name")
    public UserDTO updateUser(int userId, UserDTO userDTO) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new AppEx(ErrorCode.USER_NOT_FOUND));

        // Validate email domain
        if (userDTO.getEmail() != null && !isValidEmailDomain(userDTO.getEmail())) {
            throw new AppEx(ErrorCode.INVALID_EMAIL_DOMAIN);
        }

        // Cập nhật các trường ngoài roles và password
        if (userDTO.getUserName() != null) {
            user.setUserName(userDTO.getUserName());
        }
        if (userDTO.getFullName() != null) {
            user.setFullName(userDTO.getFullName());
        }
        if (userDTO.getEmail() != null) {
            user.setEmail(userDTO.getEmail());
        }

        // Không thay đổi roles
        // Nếu có password, thì mã hóa và thay đổi password
        if (userDTO.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        }

        // Save the updated user entity and return DTO
        user = userRepository.save(user);
        return userMapper.toDTO(user);
    }






    @PreAuthorize("hasRole('ADMIN')")
    public void deleteUser(int userId) {
        if (!userRepository.existsById(userId)) {
            throw new AppEx(ErrorCode.RESOURCE_NOT_FOUND);
        }
        userRepository.deleteById(userId);
    }
}
