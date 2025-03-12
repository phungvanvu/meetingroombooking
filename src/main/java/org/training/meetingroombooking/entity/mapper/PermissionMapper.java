package org.training.meetingroombooking.entity.mapper;

import org.mapstruct.Mapper;
import org.training.meetingroombooking.entity.dto.PermissionDTO;
import org.training.meetingroombooking.entity.models.Permission;

@Mapper(componentModel = "spring")
public interface PermissionMapper {
    Permission toEntity(PermissionDTO request);
    PermissionDTO toDTO(Permission permission);
}