package org.training.meetingroombooking.service;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import org.training.meetingroombooking.entity.dto.RoleDTO;
import org.training.meetingroombooking.entity.enums.ErrorCode;
import org.training.meetingroombooking.entity.mapper.RoleMapper;
import org.training.meetingroombooking.entity.models.Role;
import org.training.meetingroombooking.exception.AppEx;
import org.training.meetingroombooking.repository.PermissionRepository;
import org.training.meetingroombooking.repository.RoleRepository;

@Slf4j
@Service
public class RoleService {

  private final RoleRepository roleRepository;
  private final PermissionRepository permissionRepository;
  private final RoleMapper roleMapper;

  public RoleService(RoleRepository roleRepository, PermissionRepository permissionRepository,
      RoleMapper roleMapper) {
    this.roleRepository = roleRepository;
    this.permissionRepository = permissionRepository;
    this.roleMapper = roleMapper;
  }

  public RoleDTO create(RoleDTO request) {
    if (roleRepository.existsById(request.getRoleName())) {
      throw new AppEx(ErrorCode.ROLE_EXISTS);
    }
    var role = roleMapper.toEntity(request);
    var permissions = permissionRepository.findAllById(request.getPermissions());
    var missingPermissions = request.getPermissions().stream()
        .filter(p -> permissions.stream()
            .noneMatch(dbPermission -> dbPermission.getPermissionName().equals(p)))
        .collect(Collectors.toSet());
    if (!missingPermissions.isEmpty()) {
      throw new AppEx(ErrorCode.PERMISSION_NOT_FOUND);
    }
    role.setPermissions(new HashSet<>(permissions));
    Role savedrole = roleRepository.save(role);
    return roleMapper.toDTO(savedrole);
  }

  public List<RoleDTO> getAll() {
    return roleRepository.findAll()
        .stream()
        .map(roleMapper::toDTO)
        .toList();
  }

  public RoleDTO update(String roleName, RoleDTO request) {
    Role role = roleRepository.findById(roleName)
            .orElseThrow(() -> new AppEx(ErrorCode.ROLE_NOT_FOUND));
    var permissions = permissionRepository.findAllById(request.getPermissions());
    var missingPermissions = request.getPermissions().stream()
            .filter(p -> permissions.stream()
                    .noneMatch(dbPermission -> dbPermission.getPermissionName().equals(p)))
            .collect(Collectors.toSet());
    if (!missingPermissions.isEmpty()) {
      throw new AppEx(ErrorCode.PERMISSION_NOT_FOUND);
    }
    role.setPermissions(new HashSet<>(permissions));
    role.setRoleName(request.getRoleName());
    Role updatedRole = roleRepository.save(role);
    return roleMapper.toDTO(updatedRole);
  }


  public void delete(String roleName) {
    if (!roleRepository.existsById(roleName)) {
      throw new AppEx(ErrorCode.ROLE_NOT_FOUND);
    }
    roleRepository.deleteById(roleName);
  }
}
