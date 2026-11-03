package com.recipemanager.auth;

import java.io.IOException;
import java.time.Instant;
import java.util.Map;
import java.util.UUID;

import tools.jackson.databind.json.JsonMapper;
import com.recipemanager.dto.response.ApiErrorResponse;
import com.recipemanager.user.AppUserRepository;
import com.recipemanager.user.AccountStatus;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.filter.OncePerRequestFilter;

public class SessionAuthenticationFilter extends OncePerRequestFilter {

    private static final String SESSION_PREFIX = "recipe-manager:session:";

    private final StringRedisTemplate redis;
    private final AppUserRepository users;
    private final RoleService roleService;
    private final JsonMapper objectMapper;

    public SessionAuthenticationFilter(
            StringRedisTemplate redis,
            AppUserRepository users,
            RoleService roleService,
            JsonMapper objectMapper) {
        this.redis = redis;
        this.users = users;
        this.roleService = roleService;
        this.objectMapper = objectMapper;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {
        String authorization = request.getHeader("Authorization");
        if (authorization != null && authorization.startsWith("Bearer ")) {
            try {
                authenticate(authorization.substring("Bearer ".length()).trim());
            } catch (RuntimeException exception) {
                writeUnavailable(response, request.getRequestURI());
                return;
            }
        }
        filterChain.doFilter(request, response);
    }

    private void authenticate(String token) {
        if (token.isBlank()) {
            return;
        }
        try {
            String session = redis.opsForValue().get(SESSION_PREFIX + token);
            if (session == null || !session.contains(":")) {
                return;
            }
            String[] sessionParts = session.split(":", 2);
            String rawUserId = sessionParts[0];
            String currentVersion = redis.opsForValue().get(LoginService.SESSION_VERSION_PREFIX + rawUserId);
            if (!sessionParts[1].equals(currentVersion)) {
                return;
            }
            users.findById(UUID.fromString(rawUserId))
                    .filter(user -> user.getAccountStatus() == AccountStatus.ACTIVE)
                    .ifPresent(user -> SecurityContextHolder.getContext().setAuthentication(
                            UsernamePasswordAuthenticationToken.authenticated(
                                    user.getId().toString(),
                                    null,
                                    roleService.roleNamesFor(user.getId()).stream()
                                            .map(role -> new SimpleGrantedAuthority("ROLE_" + role.name()))
                                            .toList())));
        } catch (IllegalArgumentException ignored) {
            // Invalid tokens are treated as unauthenticated and are rejected by Spring Security.
        }
    }

    private void writeUnavailable(HttpServletResponse response, String path) throws IOException {
        response.setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
        response.setContentType("application/json");
        objectMapper.writeValue(response.getWriter(), new ApiErrorResponse(
                Instant.now(),
                HttpServletResponse.SC_SERVICE_UNAVAILABLE,
                "Service Unavailable",
                "Authentication is temporarily unavailable.",
                path,
                Map.of()));
    }
}
