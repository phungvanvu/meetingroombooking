package org.training.meetingroombooking.controller;

import jakarta.validation.Valid;
import java.util.List;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.training.meetingroombooking.entity.dto.Response.ApiResponse;
import org.training.meetingroombooking.entity.dto.RoleDTO;
import org.training.meetingroombooking.service.RoleService;

@RestController
@RequestMapping("/role/v1.0")
public class RoleController {

  private final RoleService roleService;

  public RoleController(RoleService roleService) {
    this.roleService = roleService;
  }

  @PostMapping
  @PreAuthorize("hasRole('ADMIN')")
  public ApiResponse<RoleDTO> create(@Valid @RequestBody RoleDTO request) {
    return ApiResponse.<RoleDTO>builder().success(true).data(roleService.create(request)).build();
  }

  @GetMapping
  @PreAuthorize("hasRole('ADMIN')")
  public ApiResponse<List<RoleDTO>> getRoles() {
    return ApiResponse.<List<RoleDTO>>builder().success(true).data(roleService.getAll()).build();
  }

  @PutMapping("/{roleName}")
  @PreAuthorize("hasRole('ADMIN')")
  public ApiResponse<RoleDTO> update(
      @PathVariable String roleName, @Valid @RequestBody RoleDTO request) {
    RoleDTO updatedRole = roleService.update(roleName, request);
    return ApiResponse.<RoleDTO>builder().success(true).data(updatedRole).build();
  }

  @DeleteMapping("/{role}")
  @PreAuthorize("hasRole('ADMIN')")
  public ApiResponse<String> delete(@PathVariable String role) {
    roleService.delete(role);
    return ApiResponse.<String>builder().success(true).data("Role has been deleted").build();
  }
}
