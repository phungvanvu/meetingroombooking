package org.training.meetingroombooking.service;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jwt.SignedJWT;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.training.meetingroombooking.entity.dto.Request.AuthRequest;
import org.training.meetingroombooking.entity.dto.Request.IntrospectRequest;
import org.training.meetingroombooking.entity.dto.Request.LogoutRequest;
import org.training.meetingroombooking.entity.dto.Request.RefreshRequest;
import org.training.meetingroombooking.entity.dto.Response.AuthResponse;
import org.training.meetingroombooking.entity.models.InvalidatedToken;
import org.training.meetingroombooking.entity.models.User;
import org.training.meetingroombooking.exception.AppEx;
import org.training.meetingroombooking.repository.InvalidatedTokenRepository;
import org.training.meetingroombooking.repository.UserRepository;

import java.text.ParseException;
import java.util.Optional;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @InjectMocks
    private AuthService authService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private InvalidatedTokenRepository invalidatedTokenRepository;

    private static final String SECRET_KEY = "***REMOVED***";

    @BeforeEach
    void setUp() throws Exception {
        // Gán SECRETKEY bằng Reflection
        var secretKeyField = AuthService.class.getDeclaredField("SECRETKEY");
        secretKeyField.setAccessible(true);
        secretKeyField.set(authService, SECRET_KEY);
    }
    @Test
    void authenticate_ShouldReturnTokens_WhenValidCredentials() {
        User user = new User();
        user.setUserName("testUser");
        user.setPassword(new BCryptPasswordEncoder().encode("password"));

        AuthRequest request = new AuthRequest("testUser", "password");

        when(userRepository.findByUserName("testUser")).thenReturn(Optional.of(user));

        AuthResponse response = authService.authenticate(request);

        assertThat(response).isNotNull();
        assertThat(response.getAccessToken()).isNotEmpty();
        assertThat(response.getRefreshToken()).isNotEmpty();
    }

    @Test
    void authenticate_ShouldThrowException_WhenInvalidCredentials() {
        AuthRequest request = new AuthRequest("testUser", "wrongPassword");
        when(userRepository.findByUserName("testUser")).thenReturn(Optional.empty());

        assertThrows(AppEx.class, () -> authService.authenticate(request));
    }
    @Test
    void introspect_ShouldReturnValidResponse_WhenTokenIsValid() throws Exception {
        String validToken = authService.generateToken(new User(), 30);
        IntrospectRequest request = new IntrospectRequest(validToken);

        assertThat(authService.introspect(request).isValid()).isTrue();
    }
    @Test
    void logout_ShouldInvalidateToken_WhenTokenIsValid() throws Exception {
        String token = authService.generateToken(new User(), 30);
        LogoutRequest request = new LogoutRequest(token);
        SignedJWT signedJWT = SignedJWT.parse(token);

        when(invalidatedTokenRepository.save(any(InvalidatedToken.class))).thenReturn(null);

        authService.logout(request);

        verify(invalidatedTokenRepository, times(1)).save(any(InvalidatedToken.class));
    }
    @Test
    void refreshToken_ShouldReturnNewToken_WhenValid() throws Exception {
        User user = new User();
        user.setUserName("testUser");

        String oldToken = authService.generateToken(user, 30);
        RefreshRequest request = new RefreshRequest(oldToken);

        when(userRepository.findByUserName(anyString())).thenReturn(Optional.of(user));
        when(invalidatedTokenRepository.existsById(anyString())).thenReturn(false);

        AuthResponse response = authService.refreshToken(request);

        assertThat(response).isNotNull();
        assertThat(response.getAccessToken()).isNotEmpty();
    }

    @Test
    void refreshToken_ShouldThrowException_WhenTokenIsInvalid() {
        RefreshRequest request = new RefreshRequest("invalidToken");

        assertThrows(AppEx.class, () -> authService.refreshToken(request));
    }
    @Test
    void verifyToken_ShouldThrowException_WhenTokenIsInvalid() {
        assertThrows(AppEx.class, () -> authService.verifyToken("invalidToken"));
    }
    @Test
    void generateToken_ShouldReturnValidToken() throws ParseException, JOSEException {
        User user = new User();
        user.setUserName("testUser");

        String token = authService.generateToken(user, 30);
        SignedJWT signedJWT = SignedJWT.parse(token);

        assertThat(signedJWT.getJWTClaimsSet().getSubject()).isEqualTo("testUser");
        assertThat(signedJWT.getJWTClaimsSet().getIssuer()).isEqualTo("meeting-room-booking");
    }
    @Test
    void verifyToken_ShouldReturnSignedJWT_WhenTokenIsValid() throws Exception {
        User user = new User();
        user.setUserName("testUser");

        String token = authService.generateToken(user, 30);
        SignedJWT signedJWT = authService.verifyToken(token);

        assertThat(signedJWT).isNotNull();
        assertThat(signedJWT.getJWTClaimsSet().getSubject()).isEqualTo("testUser");
    }
    @Test
    void verifyToken_ShouldThrowException_WhenTokenIsExpired() {
        User user = new User();
        user.setUserName("testUser");

        String expiredToken = authService.generateToken(user, -1); // Token hết hạn
        assertThrows(AppEx.class, () -> authService.verifyToken(expiredToken));
    }
    @Test
    void logout_ShouldNotSaveInvalidatedToken_WhenTokenIsExpired() throws Exception {
        String expiredToken = authService.generateToken(new User(), -1); // Token hết hạn
        LogoutRequest request = new LogoutRequest(expiredToken);

        authService.logout(request);

        verify(invalidatedTokenRepository, never()).save(any(InvalidatedToken.class));
    }
    @Test
    void refreshToken_ShouldThrowException_WhenTokenIsBlacklisted() throws Exception {
        User user = new User();
        user.setUserName("testUser");

        String token = authService.generateToken(user, 30);
        RefreshRequest request = new RefreshRequest(token);

        when(invalidatedTokenRepository.existsById(anyString())).thenReturn(true);

        assertThrows(AppEx.class, () -> authService.refreshToken(request));
    }
}





