package org.training.meetingroombooking.controller;

import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.training.meetingroombooking.entity.dto.Response.ApiResponse;
import org.training.meetingroombooking.entity.dto.RoleDTO;
import org.training.meetingroombooking.service.RoleService;

import java.util.List;

@RestController
@RequestMapping("/role")
public class RoleController {

  private final RoleService roleService;

  public RoleController(RoleService roleService) {
    this.roleService = roleService;
  }

  @PostMapping
  @PreAuthorize("hasRole('ADMIN')")
  ApiResponse<RoleDTO> create(@Valid @RequestBody RoleDTO request) {
    return ApiResponse.<RoleDTO>builder()
        .success(true)
        .data(roleService.create(request))
        .build();
  }

  @GetMapping
  @PreAuthorize("hasRole('ADMIN')")
  ApiResponse<List<RoleDTO>> getRoles() {
    return ApiResponse.<List<RoleDTO>>builder()
        .success(true)
        .data(roleService.getAll())
        .build();
  }

  @DeleteMapping("/{role}")
  @PreAuthorize("hasRole('ADMIN')")
  ApiResponse<String> delete(@PathVariable String role) {
    roleService.delete(role);
    return ApiResponse.<String>builder()
        .success(true)
        .data("Role has been deleted")
        .build();
  }
}
