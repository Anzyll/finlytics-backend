package com.anzil.finlytics.common.exception;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(org.springframework.security.authentication.BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleBadCredentials(
            jakarta.servlet.http.HttpServletRequest request
    ) {
        return ResponseEntity.status(ErrorCode.INVALID_CREDENTIALS.getStatus())
                .body(ErrorResponse.builder()
                        .message(ErrorCode.INVALID_CREDENTIALS.getMessage())
                        .errorCode(ErrorCode.INVALID_CREDENTIALS.name())
                        .status(ErrorCode.INVALID_CREDENTIALS.getStatus().value())
                        .timestamp(LocalDateTime.now())
                        .build());
    }

    @ExceptionHandler(AppException.class)
    public ResponseEntity<ErrorResponse> handleAppException(AppException ex) {

        ErrorCode code = ex.getErrorCode();

        return ResponseEntity.status(code.getStatus())
                .body(ErrorResponse.builder()
                        .message(code.getMessage())
                        .errorCode(code.name())
                        .status(code.getStatus().value())
                        .timestamp(LocalDateTime.now())
                        .build());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex) {

        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors()
                .forEach(e -> errors.put(e.getField(), e.getDefaultMessage()));

        return ResponseEntity.badRequest().body(
                ErrorResponse.builder()
                        .message("Validation failed")
                        .errorCode("VALIDATION_ERROR")
                        .status(400)
                        .timestamp(LocalDateTime.now())
                        .errors(errors)
                        .build()
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneric(Exception ex) {

        return ResponseEntity.internalServerError().body(
                ErrorResponse.builder()
                        .message(ErrorCode.INTERNAL_SERVER_ERROR.getMessage())
                        .errorCode(ErrorCode.INTERNAL_SERVER_ERROR.name())
                        .status(500)
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }
}