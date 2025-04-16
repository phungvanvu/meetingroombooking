package org.training.meetingroombooking.configuration;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jwt.SignedJWT;

import java.text.ParseException;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.stereotype.Component;
import org.training.meetingroombooking.entity.dto.Request.IntrospectRequest;
import org.training.meetingroombooking.entity.enums.ErrorCode;
import org.training.meetingroombooking.exception.AppEx;
import org.training.meetingroombooking.service.AuthService;

@Component
public class JwtDecoder implements org.springframework.security.oauth2.jwt.JwtDecoder {

  @Value("${security.jwt.access-secret}")
  private String accessSecretKey;

  private final AuthService authService;

  public JwtDecoder(AuthService authService) {
    this.authService = authService;
  }

  @Override
  public Jwt decode(String token) {
    try {
      SignedJWT signedJWT = SignedJWT.parse(token);
      String tokenType = (String) signedJWT.getJWTClaimsSet().getClaim("token_type");
      if (tokenType == null || !tokenType.equalsIgnoreCase("access")) {
        throw new AppEx(ErrorCode.UNAUTHENTICATED);
      }
      var response = authService.introspect(IntrospectRequest.builder().token(token).build());
      if (!response.isValid()) {
        throw new AppEx(ErrorCode.UNAUTHENTICATED);
      }
      SecretKeySpec secretKeySpec = new SecretKeySpec(accessSecretKey.getBytes(), "HS384");
      NimbusJwtDecoder decoder =
          NimbusJwtDecoder.withSecretKey(secretKeySpec).macAlgorithm(MacAlgorithm.HS384).build();
      return decoder.decode(token);
    } catch (ParseException | JOSEException e) {
      throw new AppEx(ErrorCode.UNAUTHENTICATED);
    }
  }
}
