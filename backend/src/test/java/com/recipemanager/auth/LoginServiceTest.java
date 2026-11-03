package com.recipemanager.auth;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.UUID;

import com.recipemanager.user.AccountStatus;
import com.recipemanager.user.AppUser;
import com.recipemanager.user.AppUserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.crypto.password.PasswordEncoder;

class LoginServiceTest {

    @Test
    void authenticatesActiveUserAndCreatesExpiringSession() {
        StringRedisTemplate redis = org.mockito.Mockito.mock(StringRedisTemplate.class);
        ValueOperations<String, String> values = org.mockito.Mockito.mock(ValueOperations.class);
        when(redis.opsForValue()).thenReturn(values);
        AppUserRepository users = org.mockito.Mockito.mock(AppUserRepository.class);
        PasswordEncoder encoder = org.mockito.Mockito.mock(PasswordEncoder.class);
        AppUser user = new AppUser(UUID.randomUUID(), "cook@example.com", "Ada", "Cook");
        when(values.get(LoginService.SESSION_VERSION_PREFIX + user.getId())).thenReturn(null);
        user.updatePasswordHash("{bcrypt}hash");
        user.verifyEmail();
        when(users.findByEmailIgnoreCase("cook@example.com")).thenReturn(Optional.of(user));
        when(encoder.matches("Secret123!", "{bcrypt}hash")).thenReturn(true);

        LoginResponse response = new LoginService(users, encoder, redis).login(
                new LoginRequest(" Cook@Example.com ", "Secret123!"));

        assertThat(response.userId()).isEqualTo(user.getId());
        assertThat(response.sessionToken()).isNotBlank();
        assertThat(response.accountStatus()).isEqualTo(AccountStatus.ACTIVE.name());
        org.mockito.Mockito.verify(values).set(
                eq("recipe-manager:session:" + response.sessionToken()),
                eq(user.getId().toString() + ":1"),
                eq(java.time.Duration.ofHours(8)));
    }

    @Test
    void rejectsInvalidCredentialsWithoutCreatingSession() {
        StringRedisTemplate redis = org.mockito.Mockito.mock(StringRedisTemplate.class);
        AppUserRepository users = org.mockito.Mockito.mock(AppUserRepository.class);
        PasswordEncoder encoder = org.mockito.Mockito.mock(PasswordEncoder.class);
        when(users.findByEmailIgnoreCase(any())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> new LoginService(users, encoder, redis).login(
                new LoginRequest("cook@example.com", "wrong")))
                .isInstanceOf(InvalidCredentialsException.class);
        org.mockito.Mockito.verifyNoInteractions(redis);
    }
}
