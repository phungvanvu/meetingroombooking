package org.training.meetingroombooking.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.training.meetingroombooking.entity.dto.RoleDTO;
import org.training.meetingroombooking.entity.models.Permission;
import org.training.meetingroombooking.entity.models.Role;
import org.training.meetingroombooking.entity.mapper.RoleMapper;
import org.training.meetingroombooking.repository.PermissionRepository;
import org.training.meetingroombooking.repository.RoleRepository;

@ExtendWith(MockitoExtension.class)
class RoleServiceTest {

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PermissionRepository permissionRepository;

    @Mock
    private RoleMapper roleMapper;

    @InjectMocks
    private RoleService roleService;

    private RoleDTO roleDTO;
    private Role role;
    private Permission permission;

    @BeforeEach
    void setUp() {
        permission = new Permission();
        permission.setNamePermission("READ");

        roleDTO = new RoleDTO();
        roleDTO.setRoleName("Admin");
        roleDTO.setDescription("Administrator role");
        roleDTO.setPermissions(Set.of("READ"));

        role = new Role();
        role.setRoleName("Admin");
        role.setDescription("Administrator role");
        role.setPermissions(Set.of(permission));
    }

    @Test
    void ***REMOVED***CreateRole() {
        when(roleMapper.toEntity(roleDTO)).thenReturn(role);
        when(permissionRepository.findAllById(roleDTO.getPermissions())).thenReturn(List.of(permission));
        when(roleRepository.save(role)).thenReturn(role);
        when(roleMapper.toDTO(role)).thenReturn(roleDTO);

        RoleDTO createdRole = roleService.create(roleDTO);

        assertNotNull(createdRole);
        assertEquals("Admin", createdRole.getRoleName());
        assertEquals(1, createdRole.getPermissions().size());
        verify(roleRepository).save(role);
    }

    @Test
    void ***REMOVED***GetAllRoles() {
        when(roleRepository.findAll()).thenReturn(List.of(role));
        when(roleMapper.toDTO(role)).thenReturn(roleDTO);

        List<RoleDTO> roles = roleService.getAll();

        assertFalse(roles.isEmpty());
        assertEquals(1, roles.size());
        assertEquals("Admin", roles.get(0).getRoleName());
    }

    @Test
    void ***REMOVED***DeleteRole() {
        doNothing().when(roleRepository).deleteById("Admin");

        roleService.delete("Admin");

        verify(roleRepository).deleteById("Admin");
    }
}
