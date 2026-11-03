package com.recipemanager.dto.response;

import java.time.Instant;

/**
 * Common envelope for successful API responses.
 *
 * @param <T> response payload type
 */
public record ApiResponse<T>(
        boolean success,
        T data,
        String message,
        Instant timestamp) {

    public static <T> ApiResponse<T> of(T data) {
        return new ApiResponse<>(true, data, null, Instant.now());
    }

    public static <T> ApiResponse<T> of(T data, String message) {
        return new ApiResponse<>(true, data, message, Instant.now());
    }
}
