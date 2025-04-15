package org.training.meetingroombooking.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.training.meetingroombooking.entity.dto.PermissionDTO;
import org.training.meetingroombooking.entity.models.Permission;
import org.training.meetingroombooking.entity.mapper.PermissionMapper;
import org.training.meetingroombooking.exception.AppEx;
import org.training.meetingroombooking.repository.PermissionRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PermissionServiceTest {

    @Mock
    private PermissionRepository permissionRepository;

    @Mock
    private PermissionMapper permissionMapper;

    @InjectMocks
    private PermissionService permissionService;

    private Permission permission;
    private PermissionDTO permissionDTO;

    @BeforeEach
    void setUp() {
        permission = new Permission();
        permission.setPermissionName("READ_PRIVILEGES");
        permission.setDescription("Allows reading data");

        permissionDTO = new PermissionDTO();
        permissionDTO.setPermissionName("READ_PRIVILEGES");
        permissionDTO.setDescription("Allows reading data");
    }

    @Test
    void ***REMOVED***CreatePermission() {
        when(permissionMapper.toEntity(permissionDTO)).thenReturn(permission);
        when(permissionRepository.save(permission)).thenReturn(permission);
        when(permissionMapper.toDTO(permission)).thenReturn(permissionDTO);

        PermissionDTO createdPermission = permissionService.create(permissionDTO);

        assertNotNull(createdPermission);
        assertEquals("READ_PRIVILEGES", createdPermission.getPermissionName());
        assertEquals("Allows reading data", createdPermission.getDescription());

        verify(permissionRepository, times(1)).save(permission);
        verify(permissionMapper, times(1)).toEntity(permissionDTO);
        verify(permissionMapper, times(1)).toDTO(permission);
    }


    @Test
    void ***REMOVED***GetAllPermissions() {
        when(permissionRepository.findAll()).thenReturn(List.of(permission));
        when(permissionMapper.toDTO(permission)).thenReturn(permissionDTO);

        List<PermissionDTO> permissions = permissionService.getAll();

        assertNotNull(permissions);
        assertEquals(1, permissions.size());
        assertEquals("READ_PRIVILEGES", permissions.get(0).getPermissionName());

        verify(permissionRepository, times(1)).findAll();
        verify(permissionMapper, times(1)).toDTO(permission);
    }

    @Test
    void ***REMOVED***UpdatePermission() {
        when(permissionRepository.findById("READ_PRIVILEGES")).thenReturn(java.util.Optional.of(permission));
        doNothing().when(permissionMapper).updateEntity(permission, permissionDTO);
        when(permissionRepository.save(permission)).thenReturn(permission);
        when(permissionMapper.toDTO(permission)).thenReturn(permissionDTO);

        PermissionDTO updatedPermission = permissionService.update("READ_PRIVILEGES", permissionDTO);

        assertNotNull(updatedPermission);
        assertEquals("READ_PRIVILEGES", updatedPermission.getPermissionName());
        assertEquals("Allows reading data", updatedPermission.getDescription());

        verify(permissionRepository, times(1)).findById("READ_PRIVILEGES");
        verify(permissionMapper, times(1)).updateEntity(permission, permissionDTO);
        verify(permissionRepository, times(1)).save(permission);
        verify(permissionMapper, times(1)).toDTO(permission);
    }


    @Test
    void ***REMOVED***DeletePermission() {
        when(permissionRepository.existsById("READ_PRIVILEGES")).thenReturn(true);
        doNothing().when(permissionRepository).deleteById("READ_PRIVILEGES");

        permissionService.delete("READ_PRIVILEGES");

        verify(permissionRepository, times(1)).deleteById("READ_PRIVILEGES");
    }


    @Test
    void ***REMOVED***UpdatePermissionNotFound() {
        when(permissionRepository.findById("READ_PRIVILEGES")).thenReturn(java.util.Optional.empty());
        assertThrows(AppEx.class, () -> permissionService.update("READ_PRIVILEGES", permissionDTO));
    }

    @Test
    void ***REMOVED***DeletePermissionNotFound() {
        when(permissionRepository.existsById("READ_PRIVILEGES")).thenReturn(false);
        assertThrows(AppEx.class, () -> permissionService.delete("READ_PRIVILEGES"));
    }


}
