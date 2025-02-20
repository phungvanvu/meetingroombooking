package org.training.meetingroombooking.service;

import java.util.HashSet;
import java.util.List;

import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import org.training.meetingroombooking.dto.RoleDTO;
import org.training.meetingroombooking.mapper.RoleMapper;
import org.training.meetingroombooking.repository.PermissionRepository;
import org.training.meetingroombooking.repository.RoleRepository;

@Slf4j
@Service
public class RoleService {
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final RoleMapper roleMapper;

    public RoleService(RoleRepository roleRepository, PermissionRepository permissionRepository, RoleMapper roleMapper) {
        this.roleRepository = roleRepository;
        this.permissionRepository = permissionRepository;
        this.roleMapper = roleMapper;
    }

    public RoleDTO create(RoleDTO roleDTO) {
        var role = roleMapper.toEntity(roleDTO);
        var permissions = permissionRepository.findAllById(roleDTO.getPermissions());
        role.setPermissions(new HashSet<>(permissions));
        role = roleRepository.save(role);
        return roleMapper.toDTO(role);
    }

    public List<RoleDTO> getAll() {
        return roleRepository.findAll().stream().map(roleMapper::toDTO).toList();
    }

    public void delete(String role) {
        roleRepository.deleteById(role);
    }
}