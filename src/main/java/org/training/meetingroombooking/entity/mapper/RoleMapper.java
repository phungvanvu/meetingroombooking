package org.training.meetingroombooking.entity.mapper;

import org.training.meetingroombooking.entity.dto.RoleDTO;
import org.training.meetingroombooking.entity.models.Role;

public interface RoleMapper {
    Role toEntity(RoleDTO roleDTO);
    RoleDTO toDTO(Role role);
    void updateEntity(Role role, RoleDTO roleDTO);
}
