package org.training.meetingroombooking.entity.mapper;

import org.training.meetingroombooking.dto.PermissionDTO;

public interface PermissionMapper {
    Permission toEntity(PermissionDTO permissionDTO);
    PermissionDTO toDTO(Permission permission);
    void updateEntity(Permission permission, PermissionDTO permissionDTO);
}
