package org.training.meetingroombooking.service;

import org.springframework.stereotype.Service;
import org.training.meetingroombooking.entity.dto.PermissionDTO;
import org.training.meetingroombooking.entity.enums.ErrorCode;
import org.training.meetingroombooking.entity.models.Permission;
import org.training.meetingroombooking.entity.mapper.PermissionMapper;
import org.training.meetingroombooking.exception.AppEx;
import org.training.meetingroombooking.repository.PermissionRepository;

import java.util.List;

@Service
public class PermissionService {

    private final PermissionRepository permissionRepository;
    private final PermissionMapper permissionMapper;

    public PermissionService(PermissionRepository permissionRepository,
                             PermissionMapper permissionMapper) {
        this.permissionRepository = permissionRepository;
        this.permissionMapper = permissionMapper;
    }

    public PermissionDTO create(PermissionDTO permissionDTO) {
        Permission permission = permissionMapper.toEntity(permissionDTO);
        Permission savedPermission = permissionRepository.save(permission);
        return permissionMapper.toDTO(savedPermission);
    }

    public List<PermissionDTO> getAll() {
        var permissions = permissionRepository.findAll();
        return permissions.stream().
                map(permissionMapper::toDTO)
                .toList();
    }

    public PermissionDTO update(String permissionName, PermissionDTO dto) {
        Permission permission = permissionRepository.findById(permissionName)
                .orElseThrow(() -> new AppEx(ErrorCode.PERMISSION_NOT_FOUND));
        permissionMapper.updateEntity(permission, dto);
        Permission updatedPermission = permissionRepository.save(permission);
        return permissionMapper.toDTO(updatedPermission);
    }

    public void delete(String permission) {
        if (!permissionRepository.existsById(permission)) {
            throw new AppEx(ErrorCode.PERMISSION_NOT_FOUND);
        }
        permissionRepository.deleteById(permission);
    }
}
