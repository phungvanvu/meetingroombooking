package org.training.meetingroombooking.entity.mapper;

import org.training.meetingroombooking.dto.RoleDTO;

public interface RoleMapper {
    Role toEntity(RoleDTO roleDTO);
    RoleDTO toDTO(Role role);
    void updateEntity(Role role, RoleDTO roleDTO);
}
