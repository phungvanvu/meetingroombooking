package org.training.meetingroombooking.mapper;

import org.springframework.stereotype.Component;
import org.training.meetingroombooking.dto.RoleDTO;
import org.training.meetingroombooking.entity.Role;

import java.util.stream.Collectors;

@Component
public class RoleMapperImpl implements RoleMapper {

    @Override
    public Role toEntity(RoleDTO roleDTO) {
        if (roleDTO == null) {
            return null;
        }
        Role role = new Role();
        role.setRoleName(roleDTO.getRoleName());
        role.setDescription(roleDTO.getDescription());
        return role;
    }

    @Override
    public RoleDTO toDTO(Role role) {
        if (role == null) {
            return null;
        }
        RoleDTO roleDTO = new RoleDTO();
        roleDTO.setRoleName(role.getRoleName());
        roleDTO.setDescription(role.getDescription());

        if (role.getPermissions() != null) {
            roleDTO.setPermissions(role.getPermissions().stream()
                    .map(permission -> permission.getNamePermission())
                    .collect(Collectors.toSet()));
        }

        return roleDTO;
    }

    @Override
    public void updateEntity(Role role, RoleDTO roleDTO) {
        if (roleDTO == null || role == null) {
            return;
        }
        role.setRoleName(roleDTO.getRoleName());
        role.setDescription(roleDTO.getDescription());
    }
}
