package com.recipemanager.auth;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.UUID;

import com.recipemanager.user.AppUser;
import com.recipemanager.user.AppUserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

class AdminUserServiceTest {

    @Test
    void deactivatesAccountAndReturnsCurrentStatus() {
        AppUserRepository users = org.mockito.Mockito.mock(AppUserRepository.class);
        StringRedisTemplate redis = org.mockito.Mockito.mock(StringRedisTemplate.class);
        ValueOperations<String, String> values = org.mockito.Mockito.mock(ValueOperations.class);
        when(redis.opsForValue()).thenReturn(values);
        UUID id = UUID.randomUUID();
        when(values.increment(LoginService.SESSION_VERSION_PREFIX + id)).thenReturn(1L);
        AppUser user = new AppUser(id, "cook@example.com", "Ada", "Cook");
        user.verifyEmail();
        when(users.findById(id)).thenReturn(Optional.of(user));

        AccountStatusResponse response = new AdminUserService(users, redis).deactivate(id);

        assertThat(response.accountStatus()).isEqualTo("INACTIVE");
        verify(users).save(any(AppUser.class));
        verify(redis.opsForValue()).increment(LoginService.SESSION_VERSION_PREFIX + id);
        verify(values).set(LoginService.SESSION_VERSION_PREFIX + id, "2");
    }

    @Test
    void activatesInactiveAccountAndReturnsCurrentStatus() {
        AppUserRepository users = org.mockito.Mockito.mock(AppUserRepository.class);
        StringRedisTemplate redis = org.mockito.Mockito.mock(StringRedisTemplate.class);
        ValueOperations<String, String> values = org.mockito.Mockito.mock(ValueOperations.class);
        when(redis.opsForValue()).thenReturn(values);
        UUID id = UUID.randomUUID();
        AppUser user = new AppUser(id, "cook@example.com", "Ada", "Cook");
        user.verifyEmail();
        user.deactivate();
        when(users.findById(id)).thenReturn(Optional.of(user));
        when(values.increment(LoginService.SESSION_VERSION_PREFIX + id)).thenReturn(2L);

        AccountStatusResponse response = new AdminUserService(users, redis).activate(id);

        assertThat(response.accountStatus()).isEqualTo("ACTIVE");
        verify(users).save(any(AppUser.class));
        verify(redis.opsForValue()).increment(LoginService.SESSION_VERSION_PREFIX + id);
    }
}
