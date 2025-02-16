package org.training.meetingroombooking.controller;

import com.nimbusds.jose.JOSEException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.training.meetingroombooking.dto.Request.LogoutRequest;
import org.training.meetingroombooking.dto.Request.RefreshRequest;
import org.training.meetingroombooking.dto.Response.ApiResponse;
import org.training.meetingroombooking.dto.Request.AuthRequest;
import org.training.meetingroombooking.dto.Response.AuthResponse;
import org.training.meetingroombooking.dto.Request.IntrospectRequest;
import org.training.meetingroombooking.dto.Response.IntrospectResponse;
import org.training.meetingroombooking.service.AuthService;

import java.text.ParseException;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    ApiResponse<AuthResponse> auth(@RequestBody AuthRequest request) {
        var result = authService.authenticate(request);
        return ApiResponse.<AuthResponse>builder()
                .success(true)
                .data(AuthResponse.builder()
                        .token(result.getToken())
                        .build())
                .build();
    }

    @PostMapping("/introspect")
    ApiResponse<IntrospectResponse> auth(@RequestBody IntrospectRequest request)
            throws ParseException, JOSEException {
        var result = authService.introspect(request);

        return ApiResponse.<IntrospectResponse>builder()
                .success(true)
                .data(result)
                .build();

    }
    @PostMapping("/logout")
    ApiResponse<Void> logout(@RequestBody LogoutRequest request)
            throws ParseException, JOSEException {
        authService.logout(request);
        return ApiResponse.<Void>builder()
                .build();
    }

    @PostMapping("/refresh")
    ApiResponse<AuthResponse> authenticate(@RequestBody RefreshRequest request)
            throws ParseException, JOSEException {
        var result = authService.refreshToken(request);
        return ApiResponse.<AuthResponse>builder().data(result).build();
    }
}
