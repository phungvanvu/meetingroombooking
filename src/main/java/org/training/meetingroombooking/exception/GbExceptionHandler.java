package org.training.meetingroombooking.exception;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
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
  public ResponseEntity<ApiResponse<Map<String, List<String>>>> handleValidation(
          MethodArgumentNotValidException ex) {
    Map<String, List<String>> errors = ex.getBindingResult()
            .getFieldErrors().stream()
            .collect(Collectors.groupingBy(
                    FieldError::getField,
                    Collectors.mapping(FieldError::getDefaultMessage, Collectors.toList())
            ));
    ApiError apiError = new ApiError(ErrorCode.VALIDATION_ERROR.getMessage());
    return ResponseEntity
            .status(ErrorCode.VALIDATION_ERROR.getStatus())
            .body(ApiResponse.error(apiError, errors));
  }

  @ExceptionHandler(value = Exception.class)
  public ResponseEntity<ApiResponse<?>> handleGeneralException(Exception ex) {
    ex.printStackTrace();
    return ResponseEntity.status(500)
        .body(ApiResponse.error(ErrorCode.INTERNAL_SERVER_ERROR.toApiError(ex.getMessage())));
  }
}
