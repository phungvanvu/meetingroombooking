package org.training.meetingroombooking.controller;

import org.springframework.web.bind.annotation.*;
import org.training.meetingroombooking.dto.Response.ApiResponse;
import org.training.meetingroombooking.dto.RoleDTO;
import org.training.meetingroombooking.service.RoleService;

import java.util.List;

@RestController
@RequestMapping("/roles")
public class RoleController {
    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }
    @PostMapping
    ApiResponse<RoleDTO> create(@RequestBody RoleDTO request) {
        return ApiResponse.<RoleDTO>builder()
                .success(true)
                .data(roleService.create(request))
                .build();
    }
    @GetMapping
    ApiResponse<List<RoleDTO>> getAll() {
        return ApiResponse.<List<RoleDTO>>builder()
                .success(true)
                .data(roleService.getAll())
                .build();
    }

    @DeleteMapping("/{role}")
    ApiResponse<Void> delete(@PathVariable String role) {
        roleService.delete(role);
        return ApiResponse.<Void>builder().success(true).build();
    }
}
