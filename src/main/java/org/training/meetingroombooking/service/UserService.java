package org.training.meetingroombooking.service;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.training.meetingroombooking.entity.dto.Request.UserRequest;
import org.training.meetingroombooking.entity.dto.Response.UserResponse;
import org.training.meetingroombooking.entity.models.Role;
import org.training.meetingroombooking.entity.models.User;
import org.training.meetingroombooking.exception.AppEx;
import org.training.meetingroombooking.entity.enums.ErrorCode;
import org.training.meetingroombooking.entity.mapper.UserMapper;
import org.training.meetingroombooking.repository.RoleRepository;
import org.training.meetingroombooking.repository.UserRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    public UserResponse createUser(UserRequest request) {
        if (userRepository.existsByUserName(request.getUserName())) {
            throw new AppEx(ErrorCode.USER_ALREADY_EXISTS);
        }

        User user = userMapper.toEntity(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        Set<Role> roles = new HashSet<>(roleRepository.findByRoleNameIn(request.getRoles()));
        if (roles.isEmpty()) {
            throw new AppEx(ErrorCode.ROLE_NOT_FOUND);
        }

        user.setRoles(roles);
        user = userRepository.save(user);
        return userMapper.toDTO(user);
    }

    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream().map(userMapper::toDTO).toList();
    }

    public UserResponse getUserById(long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppEx(ErrorCode.RESOURCE_NOT_FOUND));
        return userMapper.toDTO(user);
    }

    public UserResponse getMyInfo() {
        var context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();

        User user = userRepository.findByUserName(name).orElseThrow(
                () -> new AppEx(ErrorCode.USER_NOT_FOUND));
        return userMapper.toDTO(user);
    }

    public UserResponse updateUser(Long userId, UserRequest request) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new AppEx(ErrorCode.USER_NOT_FOUND));

        userMapper.updateEntity(user, request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        var roles = roleRepository.findAllById(request.getRoles());
        user.setRoles(new HashSet<>(roles));

        return userMapper.toDTO(userRepository.save(user));
    }

    public void deleteUser(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new AppEx(ErrorCode.RESOURCE_NOT_FOUND);
        }
        userRepository.deleteById(userId);
    }
}
