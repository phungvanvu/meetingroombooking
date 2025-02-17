package org.training.meetingroombooking.controller;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import org.training.meetingroombooking.dto.Response.ApiResponse;
import org.training.meetingroombooking.dto.PermissionDTO;
import org.training.meetingroombooking.service.PermissionService;

import java.util.List;

@RestController
@RequestMapping("/permissions")
public class PermissionController {

    private final PermissionService permissionService;

    public PermissionController(PermissionService permissionService) {
        this.permissionService = permissionService;
    }

    @PostMapping
    public ApiResponse<PermissionDTO> create(@Valid @RequestBody PermissionDTO request) {
        return ApiResponse.<PermissionDTO>builder()
                .success(true)
                .data(permissionService.create(request))
                .build();
    }

    @GetMapping
    public ApiResponse<List<PermissionDTO>> findAll() {
        return ApiResponse.<List<PermissionDTO>>builder()
                .success(true)
                .data(permissionService.getAllPermissions())
                .build();
    }

    @DeleteMapping("/{permission}")
    public ApiResponse<Void> delete(@PathVariable String permission) {
        permissionService.deletePermission(permission);
        return ApiResponse.<Void>builder().success(true).build();
    }
}
