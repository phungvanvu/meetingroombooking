package org.training.meetingroombooking.entity.mapper;

import org.training.meetingroombooking.entity.dto.PermissionDTO;
import org.training.meetingroombooking.entity.models.Permission;

public interface PermissionMapper {
    Permission toEntity(PermissionDTO permissionDTO);
    PermissionDTO toDTO(Permission permission);
    void updateEntity(Permission permission, PermissionDTO permissionDTO);
}
