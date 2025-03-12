package org.training.meetingroombooking.entity.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.training.meetingroombooking.entity.dto.Request.UserRequest;
import org.training.meetingroombooking.entity.dto.Response.UserResponse;
import org.training.meetingroombooking.entity.models.Role;
import org.training.meetingroombooking.entity.models.User;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "roles", ignore = true)
    User toEntity(UserRequest request);

    @Mapping(target = "roles", source = "roles", qualifiedByName = "mapRolesToStrings")
    UserResponse toUserResponse(User user);

    @Mapping(target = "password", ignore = true)
    @Mapping(target = "roles", ignore = true)
    void updateEntity(@MappingTarget User user, UserRequest request);

    @Named("mapUserIdToUser")
    default User mapUserIdToUser(Long userId) {
        if (userId == null) {
            return null;
        }
        User user = new User();
        user.setUserId(userId);
        return user;
    }

    @Named("mapUserToUserId")
    default Long mapUserToUserId(User user) {
        return user != null ? user.getUserId() : null;
    }

    @Named("mapRolesToStrings")
    default Set<String> mapRolesToStrings(Set<Role> roles) {
        return roles != null
                ? roles.stream()
                .map(Role::getRoleName)
                .collect(Collectors.toSet())
                : null;
    }
}
