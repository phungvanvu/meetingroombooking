package org.training.meetingroombooking.service.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.training.meetingroombooking.entity.dto.RoleDTO;
import org.training.meetingroombooking.entity.enums.ErrorCode;
import org.training.meetingroombooking.entity.mapper.RoleMapper;
import org.training.meetingroombooking.entity.models.Permission;
import org.training.meetingroombooking.entity.models.Role;
import org.training.meetingroombooking.exception.AppEx;
import org.training.meetingroombooking.repository.PermissionRepository;
import org.training.meetingroombooking.repository.RoleRepository;
import org.training.meetingroombooking.service.RoleService;

@Service
public class RoleServiceImpl implements RoleService {

  private final RoleRepository roleRepository;
  private final PermissionRepository permissionRepository;
  private final RoleMapper roleMapper;

  public RoleServiceImpl(
      RoleRepository roleRepository,
      PermissionRepository permissionRepository,
      RoleMapper roleMapper) {
    this.roleRepository = roleRepository;
    this.permissionRepository = permissionRepository;
    this.roleMapper = roleMapper;
  }

  @Override
  public RoleDTO create(RoleDTO request) {
    if (roleRepository.existsById(request.getRoleName())) {
      throw new AppEx(ErrorCode.ROLE_EXISTS);
    }
    Role role = roleMapper.toEntity(request);
    List<String> permissionsList = new ArrayList<>(request.getPermissions());
    var permissions = getValidPermissions(permissionsList);
    role.setPermissions(new HashSet<>(permissions));
    Role savedRole = roleRepository.save(role);
    return roleMapper.toDTO(savedRole);
  }

  @Override
  public List<RoleDTO> getAll() {
    return roleRepository.findAll().stream().map(roleMapper::toDTO).collect(Collectors.toList());
  }

  @Override
  public RoleDTO update(String roleName, RoleDTO request) {
    Role role =
        roleRepository.findById(roleName).orElseThrow(() -> new AppEx(ErrorCode.ROLE_NOT_FOUND));
    List<String> permissionsList = new ArrayList<>(request.getPermissions());
    var permissions = getValidPermissions(permissionsList);
    role.setPermissions(new HashSet<>(permissions));
    role.setRoleName(request.getRoleName());
    Role updatedRole = roleRepository.save(role);
    return roleMapper.toDTO(updatedRole);
  }

  @Override
  public void delete(String roleName) {
    roleRepository
        .findById(roleName)
        .ifPresentOrElse(
            roleRepository::delete,
            () -> {
              throw new AppEx(ErrorCode.ROLE_NOT_FOUND);
            });
  }

  private List<Permission> getValidPermissions(List<String> permissionNames) {
    var permissions = permissionRepository.findAllById(permissionNames);
    var missingPermissions =
        permissionNames.stream()
            .filter(
                p ->
                    permissions.stream()
                        .noneMatch(dbPermission -> dbPermission.getPermissionName().equals(p)))
            .collect(Collectors.toSet());
    if (!missingPermissions.isEmpty()) {
      throw new AppEx(ErrorCode.PERMISSION_NOT_FOUND);
    }
    return permissions;
  }
}
