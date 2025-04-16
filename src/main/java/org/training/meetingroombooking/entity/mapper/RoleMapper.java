package org.training.meetingroombooking.entity.mapper;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.training.meetingroombooking.entity.dto.RoleDTO;
import org.training.meetingroombooking.entity.models.Permission;
import org.training.meetingroombooking.entity.models.Role;

@Mapper(componentModel = "spring")
public interface RoleMapper {

  @Mapping(
      target = "permissions",
      source = "permissions",
      qualifiedByName = "mapStringsToPermissions")
  Role toEntity(RoleDTO request);

  @Mapping(
      target = "permissions",
      source = "permissions",
      qualifiedByName = "mapPermissionsToStrings")
  RoleDTO toDTO(Role role);

  @Named("mapStringsToPermissions")
  default Set<Permission> mapStringsToPermissions(Set<String> permissionNames) {
    if (permissionNames == null || permissionNames.isEmpty()) {
      return new HashSet<>();
    }
    return permissionNames.stream()
        .map(
            name -> {
              Permission permission = new Permission();
              permission.setPermissionName(name);
              return permission;
            })
        .collect(Collectors.toSet());
  }

  @Named("mapPermissionsToStrings")
  default Set<String> mapPermissionsToStrings(Set<Permission> permissions) {
    if (permissions == null || permissions.isEmpty()) {
      return new HashSet<>();
    }
    return permissions.stream().map(Permission::getPermissionName).collect(Collectors.toSet());
  }
}
