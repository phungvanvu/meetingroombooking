package org.training.meetingroombooking.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.training.meetingroombooking.entity.dto.RoleDTO;
import org.training.meetingroombooking.entity.enums.ErrorCode;
import org.training.meetingroombooking.entity.mapper.RoleMapper;
import org.training.meetingroombooking.entity.models.Permission;
import org.training.meetingroombooking.entity.models.Role;
import org.training.meetingroombooking.exception.AppEx;
import org.training.meetingroombooking.repository.PermissionRepository;
import org.training.meetingroombooking.repository.RoleRepository;

class RoleServiceTest {

  @Mock private RoleRepository roleRepository;

  @Mock private PermissionRepository permissionRepository;

  @Mock private RoleMapper roleMapper;

  private RoleService roleService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    roleService = new RoleService(roleRepository, permissionRepository, roleMapper);
  }

  @Test
  void ***REMOVED***CreateRole_Success() {
    RoleDTO request = new RoleDTO("ADMIN", "Admin role", Set.of("READ", "WRITE"));

    Permission permissionRead = mock(Permission.class);
    when(permissionRead.getPermissionName()).thenReturn("READ");

    Permission permissionWrite = mock(Permission.class);
    when(permissionWrite.getPermissionName()).thenReturn("WRITE");

    Role role = new Role("ADMIN", "Admin role", Set.of(permissionRead, permissionWrite));
    Role savedRole = new Role("ADMIN", "Admin role", Set.of(permissionRead, permissionWrite));
    RoleDTO roleDTO = new RoleDTO("ADMIN", "Admin role", Set.of("READ", "WRITE"));

    when(roleRepository.existsById("ADMIN")).thenReturn(false);
    when(permissionRepository.findAllById(Set.of("READ", "WRITE")))
        .thenReturn(List.of(permissionRead, permissionWrite));
    when(roleMapper.toEntity(request)).thenReturn(role);
    when(roleRepository.save(role)).thenReturn(savedRole);
    when(roleMapper.toDTO(savedRole)).thenReturn(roleDTO);

    RoleDTO result = roleService.create(request);

    assertNotNull(result);
    assertEquals("ADMIN", result.getRoleName());
    assertEquals("Admin role", result.getDescription());
    assertEquals(2, result.getPermissions().size());
    verify(roleRepository).save(role);
  }

  @Test
  void ***REMOVED***CreateRole_RoleExists() {
    // Arrange
    RoleDTO request = new RoleDTO("ADMIN", "Admin role", Set.of("READ", "WRITE"));

    when(roleRepository.existsById("ADMIN")).thenReturn(true);

    // Act & Assert
    AppEx exception =
        assertThrows(
            AppEx.class,
            () -> {
              roleService.create(request);
            });
    assertEquals(ErrorCode.ROLE_EXISTS, exception.getErrorCode());
  }

  @Test
  void ***REMOVED***UpdateRole_Success() {
    // Arrange
    RoleDTO request = new RoleDTO("ADMIN", "Updated Admin role", Set.of("READ", "WRITE"));

    // Mock Permission objects
    Permission permissionRead = mock(Permission.class);
    when(permissionRead.getPermissionName()).thenReturn("READ");

    Permission permissionWrite = mock(Permission.class);
    when(permissionWrite.getPermissionName()).thenReturn("WRITE");

    Role existingRole = new Role("ADMIN", "Old Admin role", Set.of(permissionRead));
    Role updatedRole =
        new Role("ADMIN", "Updated Admin role", Set.of(permissionRead, permissionWrite));

    RoleDTO roleDTO = new RoleDTO("ADMIN", "Updated Admin role", Set.of("READ", "WRITE"));

    when(roleRepository.findById("ADMIN")).thenReturn(Optional.of(existingRole));
    when(permissionRepository.findAllById(Set.of("READ", "WRITE")))
        .thenReturn(List.of(permissionRead, permissionWrite));
    when(roleRepository.save(updatedRole)).thenReturn(updatedRole);
    when(roleMapper.toDTO(updatedRole)).thenReturn(roleDTO);

    // Act
    RoleDTO result = roleService.update("ADMIN", request);

    // Assert
    assertNotNull(result);
    assertEquals("ADMIN", result.getRoleName());
    assertEquals("Updated Admin role", result.getDescription());
    assertEquals(2, result.getPermissions().size());
    verify(roleRepository).save(updatedRole);
  }

  @Test
  void ***REMOVED***UpdateRole_NotFound() {
    // Arrange
    RoleDTO request = new RoleDTO("ADMIN", "Updated Admin role", Set.of("READ", "WRITE"));

    when(roleRepository.findById("ADMIN")).thenReturn(Optional.empty());

    // Act & Assert
    AppEx exception =
        assertThrows(
            AppEx.class,
            () -> {
              roleService.update("ADMIN", request);
            });
    assertEquals(ErrorCode.ROLE_NOT_FOUND, exception.getErrorCode());
  }

  @Test
  void ***REMOVED***DeleteRole_Success() {
    // Arrange
    when(roleRepository.existsById("ADMIN")).thenReturn(true);

    // Act
    roleService.delete("ADMIN");

    // Assert
    verify(roleRepository).deleteById("ADMIN");
  }

  @Test
  void ***REMOVED***DeleteRole_NotFound() {
    // Arrange
    when(roleRepository.existsById("ADMIN")).thenReturn(false);

    // Act & Assert
    AppEx exception =
        assertThrows(
            AppEx.class,
            () -> {
              roleService.delete("ADMIN");
            });
    assertEquals(ErrorCode.ROLE_NOT_FOUND, exception.getErrorCode());
  }

  @Test
  void ***REMOVED***GetAllRoles_Success() {
    // Arrange
    Permission permissionRead = mock(Permission.class);
    when(permissionRead.getPermissionName()).thenReturn("READ");

    Permission permissionWrite = mock(Permission.class);
    when(permissionWrite.getPermissionName()).thenReturn("WRITE");

    Role role = new Role("ADMIN", "Admin role", Set.of(permissionRead, permissionWrite));
    RoleDTO roleDTO = new RoleDTO("ADMIN", "Admin role", Set.of("READ", "WRITE"));
    List<Role> roles = List.of(role);

    when(roleRepository.findAll()).thenReturn(roles);
    when(roleMapper.toDTO(role)).thenReturn(roleDTO);

    // Act
    List<RoleDTO> result = roleService.getAll();

    // Assert
    assertNotNull(result);
    assertEquals(1, result.size());
    assertEquals("ADMIN", result.get(0).getRoleName());
    assertEquals("Admin role", result.get(0).getDescription());
    assertTrue(result.get(0).getPermissions().contains("READ"));
    assertTrue(result.get(0).getPermissions().contains("WRITE"));

    verify(roleRepository).findAll();
    verify(roleMapper).toDTO(role);
  }
}
