package com.recipemanager.auth;

import java.io.IOException;
import java.util.UUID;

import com.recipemanager.user.AppUserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

public class SessionAuthenticationFilter extends OncePerRequestFilter {

    private static final String SESSION_PREFIX = "recipe-manager:session:";

    private final StringRedisTemplate redis;
    private final AppUserRepository users;

    public SessionAuthenticationFilter(StringRedisTemplate redis, AppUserRepository users) {
        this.redis = redis;
        this.users = users;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {
        String authorization = request.getHeader("Authorization");
        if (authorization != null && authorization.startsWith("Bearer ")) {
            authenticate(authorization.substring("Bearer ".length()).trim());
        }
        filterChain.doFilter(request, response);
    }

    private void authenticate(String token) {
        if (token.isBlank()) {
            return;
        }
        try {
            String rawUserId = redis.opsForValue().get(SESSION_PREFIX + token);
            if (rawUserId == null) {
                return;
            }
            users.findById(UUID.fromString(rawUserId)).ifPresent(user ->
                    SecurityContextHolder.getContext().setAuthentication(
                            UsernamePasswordAuthenticationToken.authenticated(
                                    user.getId().toString(), null, java.util.List.of())));
        } catch (IllegalArgumentException ignored) {
            // Invalid tokens are treated as unauthenticated and are rejected by Spring Security.
        }
    }
}
