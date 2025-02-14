package org.training.meetingroombooking.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.training.meetingroombooking.dto.ApiResponse;
import org.training.meetingroombooking.dto.UserDTO;
import org.training.meetingroombooking.model.User;
import org.training.meetingroombooking.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping
    ApiResponse<User> addUser(@RequestBody UserDTO userDTO) {
        ApiResponse<User> apiResponse = new ApiResponse<>();
        apiResponse.setData(userService.CreateUser(userDTO));
        return apiResponse;
    }

    @GetMapping
    List<User> getUser(){
        return userService.getAllUsers();
    }

    @GetMapping("/{userId}")
    User getUser(@PathVariable("userId") int userId) {

        return userService.getUserById(userId);
    }

    @PutMapping("/{userId}")
    User updateUser(@PathVariable int userId, @RequestBody UserDTO userDTO) {
        return userService.updateUser(userId, userDTO);
    }

    @DeleteMapping("/{userId}")
    String deleteUser(@PathVariable int userId) {
        userService.deleteUser(userId);
        return "User deleted successfully";
    }

}

