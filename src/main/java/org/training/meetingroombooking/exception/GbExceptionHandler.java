package org.training.meetingroombooking.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.training.meetingroombooking.dto.Response.ApiResponse;

@ControllerAdvice
public class GbExceptionHandler {

    @ExceptionHandler(value = AppEx.class)
    public ResponseEntity<ApiResponse<?>> handleAppException(AppEx ex) {
        return ResponseEntity.badRequest().body(ApiResponse.error(ex.getErrorCode().toApiError(ex.getMessage())));
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<?>> handleValidationException(MethodArgumentNotValidException ex) {
        String errorMessage = ex.getBindingResult().getFieldError().getDefaultMessage();
        return ResponseEntity.badRequest().body(ApiResponse.error(ErrorCode.VALIDATION_ERROR.toApiError(errorMessage)));
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<ApiResponse<?>> handleGeneralException(Exception ex) {
        ex.printStackTrace();
        return ResponseEntity.status(500)
                .body(ApiResponse.error(ErrorCode.INTERNAL_SERVER_ERROR.toApiError(ex.getMessage())));
    }

}
