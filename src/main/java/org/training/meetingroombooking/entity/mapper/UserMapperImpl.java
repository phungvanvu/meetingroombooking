package org.training.meetingroombooking.entity.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.training.meetingroombooking.entity.dto.Request.UserRequest;
import org.training.meetingroombooking.entity.dto.Response.UserResponse;
import org.training.meetingroombooking.entity.models.Role;
import org.training.meetingroombooking.entity.models.User;
import org.training.meetingroombooking.repository.RoleRepository;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class UserMapperImpl implements UserMapper {

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public User toEntity(UserRequest request) {
        if (request == null) {
            return null;
        }
        User user = new User();
        user.setUserName(request.getUserName());
        user.setFullName(request.getFullName());
        user.setDepartment(request.getDepartment());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        user.setEnabled(request.isEnabled());
        user.setGroup(request.getGroup());

        if (request.getRoles() != null) {
            Set<Role> roles = new HashSet<>(roleRepository.findByRoleNameIn(request.getRoles()));
            user.setRoles(roles);
        }

        return user;
    }

    @Override
    public UserResponse toDTO(User user) {
        if (user == null) {
            return null;
        }
        UserResponse response = new UserResponse();
        response.setUserName(user.getUserName());
        response.setFullName(user.getFullName());
        response.setDepartment(user.getDepartment());
        response.setEmail(user.getEmail());
        response.setEnabled(user.isEnabled());
        response.setGroup(user.getGroup());

        if (user.getRoles() != null) {
            Set<String> roleNames = user.getRoles().stream()
                    .map(Role::getRoleName)
                    .collect(Collectors.toSet());
            response.setRoles(roleNames);
        }

        return response;
    }


    @Override
    public void updateEntity(User user, UserRequest userRequest) {
        if (userRequest == null || user == null) {
            return;
        }
        if (userRequest.getFullName() != null) {
            user.setFullName(userRequest.getFullName());
        }
        if (userRequest.getDepartment() != null) {
            user.setDepartment(userRequest.getDepartment());
        }
        if (userRequest.getEmail() != null) {
            user.setEmail(userRequest.getEmail());
        }
        if (userRequest.getPassword() != null) {
            user.setPassword(userRequest.getPassword());
        }
        if (userRequest.getGroup() != null) {
            user.setGroup(userRequest.getGroup());
        }
        if (userRequest.getRoles() != null) {
            Set<Role> roles = new HashSet<>(roleRepository.findAllById(userRequest.getRoles()));
            user.setRoles(roles);
        }
    }
}
