package org.training.meetingroombooking.configuration;

import com.nimbusds.jose.JOSEException;
import java.text.ParseException;
import java.util.Objects;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.stereotype.Component;
import org.training.meetingroombooking.entity.dto.Request.IntrospectRequest;
import org.training.meetingroombooking.service.AuthService;

@Component
public class JwtDecoder implements org.springframework.security.oauth2.jwt.JwtDecoder {
  @Value("${security.jwt.secret}")
  private String secretKey;

  private final AuthService authService;

  public JwtDecoder(AuthService authService) {
    this.authService = authService;
  }

  private NimbusJwtDecoder nimbusJwtDecoder = null;

  @Override
  public Jwt decode(String token) throws JwtException {

    try {
      var response = authService.introspect(IntrospectRequest.builder().token(token).build());

      if (!response.isValid()) throw new JwtException("Token invalid");
    } catch (JOSEException | ParseException e) {
      throw new JwtException(e.getMessage());
    }

    if (Objects.isNull(nimbusJwtDecoder)) {
      SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes(), "HS384");
      nimbusJwtDecoder =
          NimbusJwtDecoder.withSecretKey(secretKeySpec).macAlgorithm(MacAlgorithm.HS384).build();
    }
    return nimbusJwtDecoder.decode(token);
  }
}
