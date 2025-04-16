package org.training.meetingroombooking.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.training.meetingroombooking.entity.dto.Response.ApiResponse;
import org.training.meetingroombooking.entity.enums.ErrorCode;
import org.training.meetingroombooking.exception.ApiError;

public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
  @Override
  public void commence(
      HttpServletRequest request,
      HttpServletResponse response,
      AuthenticationException authException)
      throws IOException {
    ErrorCode errorCode = ErrorCode.UNAUTHENTICATED;

    response.setContentType(MediaType.APPLICATION_JSON_VALUE);

    ApiResponse<?> apiResponse =
        ApiResponse.builder().success(false).error(new ApiError(errorCode.getMessage())).build();

    ObjectMapper objectMapper = new ObjectMapper();

    response.getWriter().write(objectMapper.writeValueAsString(apiResponse));
    response.flushBuffer();
  }
}
