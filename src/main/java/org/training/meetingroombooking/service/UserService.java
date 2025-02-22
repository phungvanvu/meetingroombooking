package org.training.meetingroombooking.service;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
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


    public UserDTO createUser(UserDTO userDTO) {
        if (userRepository.existsByUserName(userDTO.getUserName())) {
            throw new AppEx(ErrorCode.USER_ALREADY_EXISTS);
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

        return responseDTO;
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



    //@PostAuthorize("returnObject.userName == authentication.name")
    public UserDTO updateUser(int userId,  @Valid  UserDTO userDTO) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new AppEx(ErrorCode.USER_NOT_FOUND));

        // update các trường trừ roles và password
        if (userDTO.getUserName() != null) {
            user.setUserName(userDTO.getUserName());
        }
        if (userDTO.getFullName() != null) {
            user.setFullName(userDTO.getFullName());
        }
        if (userDTO.getEmail() != null) {
            user.setEmail(userDTO.getEmail());
        }

        // Nếu có password, thì mã hóa và thay đổi password
        if (userDTO.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        }

        // Save the updated user entity and return DTO
        user = userRepository.save(user);
        return userMapper.toDTO(user);
    }


    @PreAuthorize("hasRole('ADMIN') or hasRole('HR')")
    public void deleteUser(int userId) {
        if (!userRepository.existsById(userId)) {
            throw new AppEx(ErrorCode.RESOURCE_NOT_FOUND);
        }
        userRepository.deleteById(userId);
    }
}