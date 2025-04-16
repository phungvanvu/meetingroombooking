package org.training.meetingroombooking.controller;

import jakarta.validation.Valid;
import java.util.List;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.training.meetingroombooking.entity.dto.PermissionDTO;
import org.training.meetingroombooking.entity.dto.Response.ApiResponse;
import org.training.meetingroombooking.service.PermissionService;

@RestController
@RequestMapping("/permission/v1.0")
public class PermissionController {

  private final PermissionService permissionService;

  public PermissionController(PermissionService permissionService) {
    this.permissionService = permissionService;
  }

  @PostMapping
  @PreAuthorize("hasRole('ADMIN')")
  public ApiResponse<PermissionDTO> create(@Valid @RequestBody PermissionDTO request) {
    return ApiResponse.<PermissionDTO>builder()
        .success(true)
        .data(permissionService.create(request))
        .build();
  }

  @GetMapping
  @PreAuthorize("hasRole('ADMIN')")
  public ApiResponse<List<PermissionDTO>> getPermissions() {
    return ApiResponse.<List<PermissionDTO>>builder()
        .success(true)
        .data(permissionService.getAll())
        .build();
  }

  @PutMapping("/{permissionName}")
  @PreAuthorize("hasRole('ADMIN')")
  public ApiResponse<PermissionDTO> update(
      @PathVariable String permissionName, @Valid @RequestBody PermissionDTO permissionDTO) {
    PermissionDTO updatedPermission = permissionService.update(permissionName, permissionDTO);
    return ApiResponse.<PermissionDTO>builder().success(true).data(updatedPermission).build();
  }

  @DeleteMapping("/{permission}")
  @PreAuthorize("hasRole('ADMIN')")
  public ApiResponse<String> delete(@PathVariable String permission) {
    permissionService.delete(permission);
    return ApiResponse.<String>builder().success(true).data("Permission has been deleted").build();
  }
}
