package org.training.meetingroombooking.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.training.meetingroombooking.dto.UserDTO;
import org.training.meetingroombooking.entity.Role;
import org.training.meetingroombooking.entity.User;
import org.training.meetingroombooking.repository.RoleRepository;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class UserMapperImpl implements UserMapper {

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public User toEntity(UserDTO userDTO) {
        if (userDTO == null) {
            return null;
        }
        User user = new User();
        user.setUserName(userDTO.getUserName());
        user.setFullName(userDTO.getFullName());
        user.setDepartment(userDTO.getDepartment());
        user.setEmail(userDTO.getEmail());
        user.setPassword(userDTO.getPassword());
        user.setEnabled(userDTO.isEnabled());
        user.setGroup(userDTO.getGroup());

        if (userDTO.getRoles() != null) {
            Set<Role> roles = new HashSet<>(roleRepository.findByRoleNameIn(userDTO.getRoles()));
            user.setRoles(roles);
        }

        return user;
    }

    @Override
    public UserDTO toDTO(User user) {
        if (user == null) {
            return null;
        }
        UserDTO userDTO = new UserDTO();
        userDTO.setUserName(user.getUserName());
        userDTO.setFullName(user.getFullName());
        userDTO.setDepartment(user.getDepartment());
        userDTO.setEmail(user.getEmail());
        userDTO.setEnabled(user.isEnabled());
        userDTO.setGroup(user.getGroup());

        if (user.getRoles() != null) {
            Set<String> roleNames = user.getRoles().stream()
                    .map(Role::getRoleName)
                    .collect(Collectors.toSet());
            userDTO.setRoles(roleNames);
        }

        return userDTO;
    }


    @Override
    public void updateEntity(User user, UserDTO userDTO) {
        if (userDTO == null || user == null) {
            return;
        }
        if (userDTO.getFullName() != null) {
            user.setFullName(userDTO.getFullName());
        }
        if (userDTO.getDepartment() != null) {
            user.setDepartment(userDTO.getDepartment());
        }
        if (userDTO.getEmail() != null) {
            user.setEmail(userDTO.getEmail());
        }
        if (userDTO.getPassword() != null) {
            user.setPassword(userDTO.getPassword());
        }
        if (userDTO.getGroup() != null) {
            user.setGroup(userDTO.getGroup());
        }
        if (userDTO.getRoles() != null) {
            Set<Role> roles = new HashSet<>(roleRepository.findAllById(userDTO.getRoles()));
            user.setRoles(roles);
        }
    }
}
