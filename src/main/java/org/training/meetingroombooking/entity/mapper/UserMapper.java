package org.training.meetingroombooking.entity.mapper;

import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.training.meetingroombooking.entity.dto.Request.UserRequest;
import org.training.meetingroombooking.entity.dto.Response.UserResponse;
import org.training.meetingroombooking.entity.models.GroupEntity;
import org.training.meetingroombooking.entity.models.Position;
import org.training.meetingroombooking.entity.models.Role;
import org.training.meetingroombooking.entity.models.User;

@Mapper(componentModel = "spring")
public interface UserMapper {
  @Mapping(target = "position", source = "position", qualifiedByName = "mapStringToPosition")
  @Mapping(target = "group", source = "group", qualifiedByName = "mapStringToGroup")
  @Mapping(target = "roles", ignore = true)
  User toEntity(UserRequest request);

  @Mapping(target = "positionName", source = "position", qualifiedByName = "mapPositionToString")
  @Mapping(target = "groupName", source = "group", qualifiedByName = "mapGroupToString")
  @Mapping(target = "roles", source = "roles", qualifiedByName = "mapRolesToStrings")
  UserResponse toUserResponse(User user);

  @Mapping(target = "password", ignore = true)
  @Mapping(target = "roles", ignore = true)
  @Mapping(target = "position", source = "position", qualifiedByName = "mapStringToPosition")
  @Mapping(target = "group", source = "group", qualifiedByName = "mapStringToGroup")
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

  @Named("mapStringToPosition")
  default Position mapStringToPosition(String positionName) {
    if (positionName == null) {
      return null;
    }
    Position position = new Position();
    position.setPositionName(positionName);
    return position;
  }

  @Named("mapPositionToString")
  default String mapPositionToString(Position position) {
    return position != null ? position.getPositionName() : null;
  }

  @Named("mapStringToGroup")
  default GroupEntity mapStringToGroup(String groupName) {
    if (groupName == null) {
      return null;
    }
    GroupEntity group = new GroupEntity();
    group.setGroupName(groupName);
    return group;
  }

  @Named("mapGroupToString")
  default String mapGroupToString(GroupEntity group) {
    return group != null ? group.getGroupName() : null;
  }

  @Named("mapRolesToStrings")
  default Set<String> mapRolesToStrings(Set<Role> roles) {
    return roles != null ? roles.stream().map(Role::getRoleName).collect(Collectors.toSet()) : null;
  }
}
