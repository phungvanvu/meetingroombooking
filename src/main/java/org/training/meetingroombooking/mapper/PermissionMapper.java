package org.training.meetingroombooking.mapper;

import org.training.meetingroombooking.dto.PermissionDTO;
import org.training.meetingroombooking.entity.Permission;

public interface PermissionMapper {
    Permission toEntity(PermissionDTO permissionDTO);
    PermissionDTO toDTO(Permission permission);
    void updateEntity(Permission permission, PermissionDTO permissionDTO);
}
