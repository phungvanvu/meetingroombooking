package org.training.meetingroombooking.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.training.meetingroombooking.entity.dto.PermissionDTO;
import org.training.meetingroombooking.entity.enums.ErrorCode;
import org.training.meetingroombooking.entity.models.Permission;
import org.training.meetingroombooking.entity.mapper.PermissionMapper;
import org.training.meetingroombooking.exception.AppEx;
import org.training.meetingroombooking.repository.PermissionRepository;

import java.util.List;

@Slf4j
@Service
public class PermissionService {
    private final PermissionRepository permissionRepository;
    private final PermissionMapper permissionMapper;

    public PermissionService(PermissionRepository permissionRepository, PermissionMapper permissionMapper) {
        this.permissionRepository = permissionRepository;
        this.permissionMapper = permissionMapper;
    }

    public PermissionDTO create(PermissionDTO permissionDTO) {
        Permission permission = permissionMapper.toEntity(permissionDTO);
        permission = permissionRepository.save(permission);
        return permissionMapper.toDTO(permission);
    }

    public List<PermissionDTO> getAll() {
        var permissions = permissionRepository.findAll();
        return permissions.stream().
            map(permissionMapper::toDTO)
            .toList();
    }

    public void delete(String permission) {
        if (!permissionRepository.existsById(permission)) {
            throw new AppEx(ErrorCode.PERMISSION_NOT_FOUND);
        }
        permissionRepository.deleteById(permission);
    }
}
