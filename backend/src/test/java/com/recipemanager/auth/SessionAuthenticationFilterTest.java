package com.recipemanager.auth;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.UUID;

import com.recipemanager.user.AppUser;
import com.recipemanager.user.AppUserRepository;
import jakarta.servlet.FilterChain;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;

class SessionAuthenticationFilterTest {

    @AfterEach
    void clearContext() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void authenticatesBearerSessionForExistingUser() throws Exception {
        UUID id = UUID.randomUUID();
        AppUser user = new AppUser(id, "cook@example.com", "Ada", "Cook");
        user.verifyEmail();
        StringRedisTemplate redis = mock(StringRedisTemplate.class);
        ValueOperations<String, String> values = mock(ValueOperations.class);
        when(redis.opsForValue()).thenReturn(values);
        when(values.get("recipe-manager:session:token")).thenReturn(id + ":1");
        when(values.get(LoginService.SESSION_VERSION_PREFIX + id)).thenReturn("1");
        AppUserRepository users = mock(AppUserRepository.class);
        when(users.findById(id)).thenReturn(Optional.of(user));
        RoleService roles = mock(RoleService.class);
        when(roles.roleNamesFor(id)).thenReturn(java.util.Set.of(com.recipemanager.role.RoleName.USER));
        FilterChain chain = mock(FilterChain.class);
        SessionAuthenticationFilter filter = new SessionAuthenticationFilter(redis, users, roles);
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer token");

        filter.doFilter(request, new MockHttpServletResponse(), chain);

        assertThat(SecurityContextHolder.getContext().getAuthentication().isAuthenticated()).isTrue();
        assertThat(SecurityContextHolder.getContext().getAuthentication().getName()).isEqualTo(id.toString());
    }

    @Test
    void doesNotAuthenticateDeactivatedUserWithExistingSession() throws Exception {
        UUID id = UUID.randomUUID();
        AppUser user = new AppUser(id, "cook@example.com", "Ada", "Cook");
        user.verifyEmail();
        user.deactivate();
        StringRedisTemplate redis = mock(StringRedisTemplate.class);
        ValueOperations<String, String> values = mock(ValueOperations.class);
        when(redis.opsForValue()).thenReturn(values);
        when(values.get("recipe-manager:session:token")).thenReturn(id + ":1");
        when(values.get(LoginService.SESSION_VERSION_PREFIX + id)).thenReturn("2");
        AppUserRepository users = mock(AppUserRepository.class);
        when(users.findById(id)).thenReturn(Optional.of(user));

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer token");
        new SessionAuthenticationFilter(redis, users, mock(RoleService.class))
                .doFilter(request, new MockHttpServletResponse(), mock(FilterChain.class));

        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
    }

    @Test
    void rejectsOldSessionAfterAccountIsReactivated() throws Exception {
        UUID id = UUID.randomUUID();
        AppUser user = new AppUser(id, "cook@example.com", "Ada", "Cook");
        user.verifyEmail();
        StringRedisTemplate redis = mock(StringRedisTemplate.class);
        ValueOperations<String, String> values = mock(ValueOperations.class);
        when(redis.opsForValue()).thenReturn(values);
        when(values.get("recipe-manager:session:token")).thenReturn(id + ":1");
        when(values.get(LoginService.SESSION_VERSION_PREFIX + id)).thenReturn("2");
        when(values.increment(LoginService.SESSION_VERSION_PREFIX + id)).thenReturn(1L, 2L);
        AppUserRepository users = mock(AppUserRepository.class);
        when(users.findById(id)).thenReturn(Optional.of(user));
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer token");

        AdminUserService adminUsers = new AdminUserService(users, redis);
        adminUsers.deactivate(id);
        adminUsers.activate(id);

        new SessionAuthenticationFilter(redis, users, mock(RoleService.class))
                .doFilter(request, new MockHttpServletResponse(), mock(FilterChain.class));

        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
    }

    @Test
    void ignoresMissingOrUnknownSession() throws Exception {
        StringRedisTemplate redis = mock(StringRedisTemplate.class);
        ValueOperations<String, String> values = mock(ValueOperations.class);
        when(redis.opsForValue()).thenReturn(values);
        when(values.get("recipe-manager:session:unknown")).thenReturn(null);
        SessionAuthenticationFilter filter = new SessionAuthenticationFilter(
                redis, mock(AppUserRepository.class), mock(RoleService.class));
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer unknown");

        filter.doFilter(request, new MockHttpServletResponse(), mock(FilterChain.class));

        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
    }
}
