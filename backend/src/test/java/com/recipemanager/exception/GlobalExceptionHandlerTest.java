package com.recipemanager.exception;

import static org.assertj.core.api.Assertions.assertThat;

import com.recipemanager.dto.response.ApiErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletRequest;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void returnsSafeBadRequestContract() {
        HttpServletRequest request = request("/api/recipes");

        ApiErrorResponse response = handler.handleBadRequest(
                new IllegalArgumentException("title is required"), request).getBody();

        assertThat(response).isNotNull();
        assertThat(response.status()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(response.message()).isEqualTo("title is required");
        assertThat(response.path()).isEqualTo("/api/recipes");
        assertThat(response.fieldErrors()).isEmpty();
    }

    @Test
    void doesNotExposeUnexpectedExceptionDetails() {
        HttpServletRequest request = request("/api/private");

        ApiErrorResponse response = handler.handleUnexpected(
                new RuntimeException("database password=do-not-return"), request).getBody();

        assertThat(response).isNotNull();
        assertThat(response.status()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
        assertThat(response.message()).isEqualTo("An unexpected error occurred");
        assertThat(response.message()).doesNotContain("password");
        assertThat(response.fieldErrors()).isEmpty();
    }

    private HttpServletRequest request(String uri) {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setMethod("GET");
        request.setRequestURI(uri);
        return request;
    }
}
