package org.training.meetingroombooking.mapper;

import org.springframework.stereotype.Component;
import org.training.meetingroombooking.dto.PermissionDTO;
import org.training.meetingroombooking.entity.Permission;

import java.util.Collections;

@Component
public class PermissionMapperImpl implements PermissionMapper {

    @Override
    public Permission toEntity(PermissionDTO permissionDTO) {
        if (permissionDTO == null) {
            return null;
        }
        return new Permission(
                permissionDTO.getNamePermission(),
                permissionDTO.getDescription(),
                Collections.emptySet()
        );
    }

    @Override
    public PermissionDTO toDTO(Permission permission) {
        if (permission == null) {
            return null;
        }
        return new PermissionDTO(
                permission.getNamePermission(),
                permission.getDescription()
        );
    }

    @Override
    public void updateEntity(Permission permission, PermissionDTO permissionDTO) {
        if (permissionDTO == null || permission == null) {
            return;
        }
        permission.setNamePermission(permissionDTO.getNamePermission());
        permission.setDescription(permissionDTO.getDescription());
    }

}
