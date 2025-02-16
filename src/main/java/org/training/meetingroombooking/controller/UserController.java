package org.training.meetingroombooking.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.training.meetingroombooking.dto.Response.ApiResponse;
import org.training.meetingroombooking.dto.UserDTO;
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
    public ApiResponse<UserDTO> createUser(@RequestBody @Valid UserDTO request) {
        return ApiResponse.<UserDTO>builder()
                        .success(true)
                        .data(userService.createUser(request))
                        .build();
    }

    @GetMapping
    ApiResponse<List<UserDTO>> getUsers() {
        return ApiResponse.<List<UserDTO>>builder()
                .success(true)
                .data(userService.getAllUsers())
                .build();
    }


    @GetMapping("/{userId}")
    ApiResponse<UserDTO> getUser(@PathVariable("userId") int userId) {
        return ApiResponse.<UserDTO>builder()
                .success(true)
                .data(userService.getUserById(userId))
                .build();
    }

    @GetMapping("/my-info")
    public ApiResponse<UserDTO> getMyInfo() {
        return ApiResponse.<UserDTO>builder()
                .success(true)
                .data(userService.getMyInfo())
                .build();
    }

    @PutMapping("/{userId}")
    ApiResponse<UserDTO> updateUser(@PathVariable int userId, @RequestBody UserDTO request) {
        return ApiResponse.<UserDTO>builder()
                .success(true)
                .data(userService.updateUser(userId, request))
                .build();
    }

    @DeleteMapping("/{userId}")
    ApiResponse<String> deleteUser(@PathVariable int userId) {
        userService.deleteUser(userId);
        return ApiResponse.<String>builder().success(true).data("User has been deleted").build();
    }
}
