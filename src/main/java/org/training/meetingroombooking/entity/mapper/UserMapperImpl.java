package org.training.meetingroombooking.entity.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.training.meetingroombooking.entity.dto.Request.UserRequest;
import org.training.meetingroombooking.entity.dto.Response.UserResponse;
import org.training.meetingroombooking.entity.models.Position;
import org.training.meetingroombooking.entity.models.Role;
import org.training.meetingroombooking.entity.models.User;
import org.training.meetingroombooking.repository.GroupRepository;
import org.training.meetingroombooking.repository.PositionRepository;
import org.training.meetingroombooking.repository.RoleRepository;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class UserMapperImpl implements UserMapper {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PositionRepository positionRepository;

    @Autowired
    private GroupRepository groupRepository;

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
        user.setPhoneNumber(request.getPhoneNumber());
        user.setPassword(request.getPassword());
        user.setEnabled(request.isEnabled());

        // Lấy Position từ repository theo tên
        if (request.getPosition() != null) {
            positionRepository.findByPositionName(request.getPosition().getPositionName())
                    .ifPresent(user::setPosition);
        }

        // Lấy GroupEntity từ repository theo tên
        if (request.getGroup() != null) {
            groupRepository.findByGroupName(request.getGroup().getGroupName())
                    .ifPresent(user::setGroup);
        }

        // Lấy danh sách Role từ repository
        if (request.getRoles() != null && !request.getRoles().isEmpty()) {
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
        response.setUserId(user.getUserId());
        response.setUserName(user.getUserName());
        response.setFullName(user.getFullName());
        response.setDepartment(user.getDepartment());
        response.setEmail(user.getEmail());
        response.setPhoneNumber(user.getPhoneNumber());
        response.setEnabled(user.isEnabled());

        // Lấy tên vị trí nếu có
        if (user.getPosition() != null) {
            response.setPositionName(user.getPosition().getPositionName());
        }

        // Lấy tên nhóm nếu có
        if (user.getGroup() != null) {
            response.setGroupName(user.getGroup().getGroupName());
        }

        // Lấy danh sách vai trò
        if (user.getRoles() != null && !user.getRoles().isEmpty()) {
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
        // Cập nhật Position nếu có
        if (userRequest.getPosition() != null && userRequest.getPosition().getPositionName() != null) {
            positionRepository.findByPositionName(userRequest.getPosition().getPositionName())
                    .ifPresent(user::setPosition);
        }
        // Cập nhật Group nếu có
        if (userRequest.getGroup() != null && userRequest.getGroup().getGroupName() != null) {
            groupRepository.findByGroupName(userRequest.getGroup().getGroupName())
                    .ifPresent(user::setGroup);
        }
        // Cập nhật Roles nếu có
        if (userRequest.getRoles() != null && !userRequest.getRoles().isEmpty()) {
            Set<Role> roles = new HashSet<>(roleRepository.findByRoleNameIn(userRequest.getRoles()));
            user.setRoles(roles);
        }
        user.setEnabled(userRequest.isEnabled());
    }

}
