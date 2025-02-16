package org.training.meetingroombooking.mapper;

import org.training.meetingroombooking.dto.RoleDTO;
import org.training.meetingroombooking.entity.Role;

public interface RoleMapper {
    Role toEntity(RoleDTO roleDTO);
    RoleDTO toDTO(Role role);
    void updateEntity(Role role, RoleDTO roleDTO);
}
