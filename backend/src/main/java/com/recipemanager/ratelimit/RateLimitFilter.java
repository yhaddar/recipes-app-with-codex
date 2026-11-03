package com.recipemanager.ratelimit;

import java.io.IOException;
import java.time.Instant;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.recipemanager.dto.response.ApiErrorResponse;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.web.filter.OncePerRequestFilter;

public class RateLimitFilter extends OncePerRequestFilter {

    private final RateLimitService rateLimitService;
    private final ClientIpResolver clientIpResolver;
    private final ObjectMapper objectMapper;

    public RateLimitFilter(
            RateLimitService rateLimitService,
            ClientIpResolver clientIpResolver,
            ObjectMapper objectMapper) {
        this.rateLimitService = rateLimitService;
        this.clientIpResolver = clientIpResolver;
        this.objectMapper = objectMapper;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {
        try {
            if (!rateLimitService.isAllowed(clientIpResolver.resolve(request))) {
                writeError(response, 429,
                        "Too many requests. Try again later.", request.getRequestURI());
                return;
            }
        } catch (RuntimeException exception) {
            writeError(response, HttpServletResponse.SC_SERVICE_UNAVAILABLE,
                    "Rate limiting is temporarily unavailable.", request.getRequestURI());
            return;
        }

        filterChain.doFilter(request, response);
    }

    private void writeError(HttpServletResponse response, int status, String message, String path) throws IOException {
        response.setStatus(status);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        ApiErrorResponse body = new ApiErrorResponse(
                Instant.now(), status, status == 429 ? "Too Many Requests" : "Service Unavailable",
                message, path, Map.of());
        objectMapper.writeValue(response.getWriter(), body);
    }
}
