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

        User user = userMapper.toEntity(userDTO);
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));

        Set<Role> roles = new HashSet<>(roleRepository.findByRoleNameIn(userDTO.getRoles()));
        if (roles.isEmpty()) {
            throw new AppEx(ErrorCode.ROLE_NOT_FOUND);
        }

        user.setRoles(roles);
        user = userRepository.save(user);
        return userMapper.toDTO(user);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream().map(userMapper::toDTO).toList();
    }

    @PreAuthorize("hasRole('ADMIN')")
    public UserDTO getUserById(int userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppEx(ErrorCode.RESOURCE_NOT_FOUND));
        return userMapper.toDTO(user);
    }

    @PostAuthorize("returnObject.userName == authentication.name")
    public UserDTO getMyInfo() {
        var context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();

        User user = userRepository.findByUserName(name).orElseThrow(
                () -> new AppEx(ErrorCode.USER_NOT_FOUND));
        return userMapper.toDTO(user);
    }

    @PostAuthorize("returnObject.userName == authentication.name")
    public UserDTO updateUser(int userId, UserDTO userDTO) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new AppEx(ErrorCode.USER_NOT_FOUND));

        userMapper.updateEntity(user, userDTO);
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));

        var roles = roleRepository.findAllById(userDTO.getRoles());
        user.setRoles(new HashSet<>(roles));

        return userMapper.toDTO(userRepository.save(user));
    }

    @PreAuthorize("hasRole('ADMIN')")
    public void deleteUser(int userId) {
        if (!userRepository.existsById(userId)) {
            throw new AppEx(ErrorCode.RESOURCE_NOT_FOUND);
        }
        userRepository.deleteById(userId);
    }
}
