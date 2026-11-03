package com.recipemanager.exception;

import java.time.Instant;
import java.util.Map;

import com.recipemanager.dto.response.ApiErrorResponse;
import com.recipemanager.auth.DuplicateResourceException;
import com.recipemanager.auth.InvalidCredentialsException;
import com.recipemanager.validation.ValidationErrorMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<ApiErrorResponse> handleValidation(
            MethodArgumentNotValidException exception,
            HttpServletRequest request) {
        return error(
                HttpStatus.BAD_REQUEST,
                "Validation failed",
                request,
                ValidationErrorMapper.toFieldErrors(exception.getBindingResult()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    ResponseEntity<ApiErrorResponse> handleBadRequest(
            IllegalArgumentException exception,
            HttpServletRequest request) {
        return error(HttpStatus.BAD_REQUEST, exception.getMessage(), request, Map.of());
    }

    @ExceptionHandler(DuplicateResourceException.class)
    ResponseEntity<ApiErrorResponse> handleConflict(
            DuplicateResourceException exception,
            HttpServletRequest request) {
        return error(HttpStatus.CONFLICT, exception.getMessage(), request, Map.of());
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    ResponseEntity<ApiErrorResponse> handleInvalidCredentials(
            InvalidCredentialsException exception,
            HttpServletRequest request) {
        return error(HttpStatus.UNAUTHORIZED, "Invalid email or password", request, Map.of());
    }

    @ExceptionHandler(AuthenticationException.class)
    ResponseEntity<ApiErrorResponse> handleAuthentication(
            AuthenticationException exception,
            HttpServletRequest request) {
        return error(HttpStatus.UNAUTHORIZED, "Authentication is required", request, Map.of());
    }

    @ExceptionHandler(AccessDeniedException.class)
    ResponseEntity<ApiErrorResponse> handleAccessDenied(
            AccessDeniedException exception,
            HttpServletRequest request) {
        return error(HttpStatus.FORBIDDEN, "Access denied", request, Map.of());
    }

    @ExceptionHandler(Exception.class)
    ResponseEntity<ApiErrorResponse> handleUnexpected(
            Exception exception,
            HttpServletRequest request) {
        LOGGER.error(
                "Unhandled API exception on {} {} ({})",
                request.getMethod(),
                request.getRequestURI(),
                exception.getClass().getName());
        return error(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred", request, Map.of());
    }

    private ResponseEntity<ApiErrorResponse> error(
            HttpStatus status,
            String message,
            HttpServletRequest request,
            Map<String, String> fieldErrors) {
        ApiErrorResponse response = new ApiErrorResponse(
                Instant.now(),
                status.value(),
                status.getReasonPhrase(),
                message,
                request.getRequestURI(),
                fieldErrors);

        return ResponseEntity.status(status).body(response);
    }
}
