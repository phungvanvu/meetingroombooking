package org.training.meetingroombooking.exception;

import java.util.Objects;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.training.meetingroombooking.entity.dto.Response.ApiResponse;
import org.training.meetingroombooking.entity.enums.ErrorCode;

@ControllerAdvice
public class GbExceptionHandler {

  @ExceptionHandler(AppEx.class)
  public ResponseEntity<ApiResponse<?>> handleAppEx(AppEx ex) {
    return ResponseEntity.status(ex.getErrorCode().getStatus())
        .body(ApiResponse.error(new ApiError(ex.getMessage())));
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ApiResponse<?>> handleValidation(MethodArgumentNotValidException ex) {
    String message = Objects.requireNonNull(ex.getFieldError()).getDefaultMessage();
    return ResponseEntity.badRequest().body(ApiResponse.error(new ApiError(message)));
  }

  @ExceptionHandler(value = Exception.class)
  public ResponseEntity<ApiResponse<?>> handleGeneralException(Exception ex) {
    ex.printStackTrace();
    return ResponseEntity.status(500)
        .body(ApiResponse.error(ErrorCode.INTERNAL_SERVER_ERROR.toApiError(ex.getMessage())));
  }
}
