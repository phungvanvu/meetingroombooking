package org.training.meetingroombooking.service;

import com.nimbusds.jose.JOSEException;

import java.text.ParseException;
import org.training.meetingroombooking.entity.dto.Request.AuthRequest;
import org.training.meetingroombooking.entity.dto.Request.IntrospectRequest;
import org.training.meetingroombooking.entity.dto.Request.LogoutRequest;
import org.training.meetingroombooking.entity.dto.Request.RefreshRequest;
import org.training.meetingroombooking.entity.dto.Response.AuthResponse;
import org.training.meetingroombooking.entity.dto.Response.IntrospectResponse;

public interface AuthService {
  IntrospectResponse introspect(IntrospectRequest request) throws JOSEException;

  AuthResponse authenticate(AuthRequest request);

  void logout(LogoutRequest request) throws ParseException, JOSEException;

  AuthResponse refreshToken(RefreshRequest request) throws ParseException, JOSEException;
}
