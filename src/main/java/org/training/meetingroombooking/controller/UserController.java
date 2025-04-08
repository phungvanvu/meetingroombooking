package org.training.meetingroombooking.controller;

import jakarta.validation.Valid;
import java.util.Set;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.training.meetingroombooking.entity.dto.Request.UserRequest;
import org.training.meetingroombooking.entity.dto.Response.ApiResponse;
import org.training.meetingroombooking.entity.dto.Response.UserResponse;
import org.training.meetingroombooking.service.UserService;

import java.util.List;

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

  @GetMapping("/search")
  public ApiResponse<Page<UserResponse>> searchUsers(
          @RequestParam(value = "fullName", required = false) String fullName,
          @RequestParam(value = "department", required = false) String department,
          @RequestParam(value = "position", required = false) Set<String> position,
          @RequestParam(value = "group", required = false) Set<String> group,
          @RequestParam(value = "roles", required = false) Set<String> roles,
          @RequestParam(value = "page", defaultValue = "0") int page,
          @RequestParam(value = "size", defaultValue = "10") int size
  ) {
    Page<UserResponse> usersPage = userService.getUsers(fullName, department, position, group, roles, page, size);
    return ApiResponse.<Page<UserResponse>>builder()
            .success(true)
            .data(usersPage)
            .build();
  }

  @GetMapping
  @PreAuthorize("hasRole('ADMIN')")
  ApiResponse<List<UserResponse>> getUsers() {
    return ApiResponse.<List<UserResponse>>builder()
        .success(true)
        .data(userService.getAll())
        .build();
  }

  @GetMapping("/{userId}")
  @PreAuthorize("hasRole('ADMIN')")
  ApiResponse<UserResponse> getUser(@PathVariable("userId") Long userId) {
    return ApiResponse.<UserResponse>builder()
        .success(true)
        .data(userService.getById(userId))
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
  ApiResponse<UserResponse> updateUser(@PathVariable Long userId,
      @Valid @RequestBody UserRequest request) {
    return ApiResponse.<UserResponse>builder()
        .success(true)
        .data(userService.update(userId, request))
        .build();
  }

  @DeleteMapping("/{userId}")
  @PreAuthorize("hasRole('ADMIN')")
  ApiResponse<String> deleteUser(@PathVariable Long userId) {
    userService.delete(userId);
    return ApiResponse.<String>builder()
        .success(true)
        .data("User has been deleted")
        .build();
  }

  @DeleteMapping("/delete-multiple")
  @PreAuthorize("hasRole('ADMIN')")
  ApiResponse<String> deleteMultipleUsers(@RequestBody List<Long> userIds) {
    userService.deleteMultipleUsers(userIds);
    return ApiResponse.<String>builder()
            .success(true)
            .data("Selected users have been deleted successfully.")
            .build();
  }
}
