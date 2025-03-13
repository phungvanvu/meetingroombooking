package org.training.meetingroombooking.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.training.meetingroombooking.entity.dto.Request.UserRequest;
import org.training.meetingroombooking.entity.dto.Response.ApiResponse;
import org.training.meetingroombooking.entity.dto.Response.UserResponse;
import org.training.meetingroombooking.service.UserService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    ApiResponse<UserResponse> createUser(@Valid @RequestBody UserRequest request) {
        return ApiResponse.<UserResponse>builder()
                        .success(true)
                        .data(userService.createUser(request))
                        .build();
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    ApiResponse<List<UserResponse>> getUsers() {
        return ApiResponse.<List<UserResponse>>builder()
                .success(true)
                .data(userService.getAllUsers())
                .build();
    }


    @GetMapping("/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    ApiResponse<UserResponse> getUser(@PathVariable("userId") Long userId) {
        return ApiResponse.<UserResponse>builder()
                .success(true)
                .data(userService.getUserById(userId))
                .build();
    }

    @GetMapping("/my-info")
    @PostAuthorize("returnObject.data.userName == authentication.name")
    public ApiResponse<UserResponse> getMyInfo() {
        return ApiResponse.<UserResponse>builder()
                .success(true)
                .data(userService.getMyInfo())
                .build();
    }

    @PutMapping("/{userId}")
    @PostAuthorize("returnObject.data.userName == authentication.name or hasRole('ADMIN')")
    ApiResponse<UserResponse> updateUser(@PathVariable Long userId, @Valid @RequestBody UserRequest request) {
        return ApiResponse.<UserResponse>builder()
                .success(true)
                .data(userService.updateUser(userId, request))
                .build();
    }

    @DeleteMapping("/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    ApiResponse<String> deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId);
        return ApiResponse.<String>builder()
            .success(true)
            .data("User has been deleted")
            .build();
    }
}
