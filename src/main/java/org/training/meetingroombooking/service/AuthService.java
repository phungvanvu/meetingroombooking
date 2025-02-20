package org.training.meetingroombooking.service;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSObject;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.Payload;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.StringJoiner;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.training.meetingroombooking.dto.Request.AuthRequest;
import org.training.meetingroombooking.dto.Request.IntrospectRequest;
import org.training.meetingroombooking.dto.Request.LogoutRequest;
import org.training.meetingroombooking.dto.Request.RefreshRequest;
import org.training.meetingroombooking.dto.Response.AuthResponse;
import org.training.meetingroombooking.dto.Response.IntrospectResponse;
import org.training.meetingroombooking.entity.InvalidatedToken;
import org.training.meetingroombooking.entity.User;
import org.training.meetingroombooking.exception.AppEx;
import org.training.meetingroombooking.exception.ErrorCode;
import org.training.meetingroombooking.repository.InvalidatedTokenRepository;
import org.training.meetingroombooking.repository.UserRepository;

@Slf4j
@Service
public class AuthService {
    private final UserRepository userRepository;
    private final InvalidatedTokenRepository invalidatedTokenRepository;

    @Value("${security.jwt.secret}")
    protected String SECRETKEY;

    public AuthService(UserRepository userRepository, InvalidatedTokenRepository invalidatedTokenRepository) {
        this.userRepository = userRepository;
        this.invalidatedTokenRepository = invalidatedTokenRepository;
    }

    public IntrospectResponse introspect(IntrospectRequest request)
            throws JOSEException, ParseException {
        var token = request.getToken();

        verifyToken(token);
        return IntrospectResponse.builder()
                .valid(true).build();
    }

    public AuthResponse authenticate(AuthRequest request) {
        var user = userRepository.findByUserName(request.getUsername())
                .orElseThrow(() -> new AppEx(ErrorCode.USER_NOT_FOUND));

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        boolean auth = passwordEncoder.matches(request.getPassword(), user.getPassword());

        if (!auth) {
            throw new AppEx(ErrorCode.UNAUTHENTICATED);
        }

        var accessToken = generateToken(user, 30);  // Access Token sống 30 phút
        var refreshToken = generateToken(user, 7 * 24 * 60);  // Refresh Token sống 7 ngày

        return AuthResponse.builder()
                .accesstoken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }


    public void logout(LogoutRequest request) throws ParseException, JOSEException {
        try {
            var signToken = verifyToken(request.getToken());

            String jit = signToken.getJWTClaimsSet().getJWTID();
            Date expiryTime = signToken.getJWTClaimsSet().getExpirationTime();

            InvalidatedToken invalidatedToken =
                    InvalidatedToken.builder().id(jit).expiryTime(expiryTime).build();

            invalidatedTokenRepository.save(invalidatedToken);
        } catch (AppEx exception) {
            log.info("Token already expired");
        }
    }

    public AuthResponse refreshToken(RefreshRequest request) throws ParseException, JOSEException {
        var signedJWT = verifyToken(request.getToken());

        // Kiểm tra token đã bị vô hiệu hóa chưa
        var jit = signedJWT.getJWTClaimsSet().getJWTID();
        if (invalidatedTokenRepository.existsById(jit)) {
            throw new AppEx(ErrorCode.UNAUTHENTICATED);
        }

        var username = signedJWT.getJWTClaimsSet().getSubject();
        var user = userRepository.findByUserName(username)
                .orElseThrow(() -> new AppEx(ErrorCode.UNAUTHENTICATED));

        var newAccessToken = generateToken(user, 30);

        return AuthResponse.builder()
                .accesstoken(newAccessToken)
                .refreshToken(request.getToken())
                .build();
    }



    protected SignedJWT verifyToken(String token) throws JOSEException {
        try {
            JWSVerifier verifier = new MACVerifier(SECRETKEY.getBytes());

            SignedJWT signedJWT = SignedJWT.parse(token);

            Date expiryTime = signedJWT.getJWTClaimsSet().getExpirationTime();

            var verified = signedJWT.verify(verifier);

            if (!(verified && expiryTime.after(new Date())))
                throw new AppEx(ErrorCode.UNAUTHENTICATED);

            if (invalidatedTokenRepository.existsById(signedJWT.getJWTClaimsSet().getJWTID()))
                throw new AppEx(ErrorCode.UNAUTHENTICATED);

            return signedJWT;
        } catch (ParseException e) {
            throw new AppEx(ErrorCode.UNAUTHENTICATED);
        }
    }



    protected String generateToken(User user, int expirationMinutes) {
        try {
            JWSHeader header = new JWSHeader(JWSAlgorithm.HS384);
            JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                    .subject(user.getUserName())
                    .issuer("meeting-room-booking")
                    .issueTime(new Date())
                    .expirationTime(Date.from(Instant.now().plus(expirationMinutes, ChronoUnit.MINUTES)))
                    .jwtID(UUID.randomUUID().toString())
                    .claim("scope", buildScope(user))
                    .build();

            JWSObject jwsObject = new JWSObject(header, new Payload(jwtClaimsSet.toJSONObject()));
            jwsObject.sign(new MACSigner(SECRETKEY.getBytes()));

            return jwsObject.serialize();
        } catch (JOSEException e) {
            log.error("Error generating token", e);
            throw new RuntimeException(e);
        }
    }

    private String buildScope(User user) {
        StringJoiner stringJoiner = new StringJoiner(" ");

        if (!CollectionUtils.isEmpty(user.getRoles()))
            user.getRoles().forEach(role -> {
                stringJoiner.add("ROLE_" + role.getRoleName());
                if (!CollectionUtils.isEmpty(role.getPermissions()))
                    role.getPermissions().forEach(permission
                            -> stringJoiner.add(permission.getNamePermission()));
            });

        return stringJoiner.toString();
    }
}

