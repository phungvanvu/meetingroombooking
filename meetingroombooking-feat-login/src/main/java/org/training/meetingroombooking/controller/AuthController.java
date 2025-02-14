package org.training.meetingroombooking.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;
import org.training.meetingroombooking.dto.ApiResponse;
import org.training.meetingroombooking.dto.AuthRequest;
import org.training.meetingroombooking.response.AuthReponse;
import org.training.meetingroombooking.service.AuthService;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final RestClient.Builder builder;
    AuthService authService;

    public AuthController(RestClient.Builder builder) {
        this.builder = builder;
    }

    @PostMapping("/login")
    public ApiResponse<AuthReponse> auth(@RequestBody AuthRequest request) {
        boolean result = authService.authenticate(request);

        return ApiResponse.<AuthReponse>builder()
                .data(AuthReponse.builder()
                        .auth(result)
                        .build())
                .message(result ? "Authentication successful" : "Authentication failed")
                .status(result)
                .build();
    }

}
