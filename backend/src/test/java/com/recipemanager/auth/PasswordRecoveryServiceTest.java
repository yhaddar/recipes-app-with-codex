package com.recipemanager.auth;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.UUID;

import com.recipemanager.email.SmtpEmailService;
import com.recipemanager.user.AppUser;
import com.recipemanager.user.AppUserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.crypto.password.PasswordEncoder;

class PasswordRecoveryServiceTest {

    @Test
    void issuesExpiringResetTokenForExistingAccount() {
        StringRedisTemplate redis = org.mockito.Mockito.mock(StringRedisTemplate.class);
        ValueOperations<String, String> values = org.mockito.Mockito.mock(ValueOperations.class);
        when(redis.opsForValue()).thenReturn(values);
        AppUserRepository users = org.mockito.Mockito.mock(AppUserRepository.class);
        AppUser user = new AppUser(UUID.randomUUID(), "cook@example.com", "Ada", "Cook");
        when(users.findByEmailIgnoreCase("cook@example.com")).thenReturn(Optional.of(user));
        SmtpEmailService mail = org.mockito.Mockito.mock(SmtpEmailService.class);

        String token = new PasswordRecoveryService(
                users, redis, mail, org.mockito.Mockito.mock(PasswordEncoder.class), "https://example.test/reset")
                .requestReset(" Cook@Example.com ");

        assertThat(token).isNotBlank();
        verify(values).set(eq("recipe-manager:password-reset:" + token), eq(user.getId().toString()), eq(java.time.Duration.ofMinutes(30)));
        verify(mail).send(any());
    }

    @Test
    void resetsPasswordAndConsumesToken() {
        StringRedisTemplate redis = org.mockito.Mockito.mock(StringRedisTemplate.class);
        ValueOperations<String, String> values = org.mockito.Mockito.mock(ValueOperations.class);
        when(redis.opsForValue()).thenReturn(values);
        UUID id = UUID.randomUUID();
        when(values.get("recipe-manager:password-reset:token")).thenReturn(id.toString());
        AppUserRepository users = org.mockito.Mockito.mock(AppUserRepository.class);
        AppUser user = new AppUser(id, "cook@example.com", "Ada", "Cook");
        when(users.findById(id)).thenReturn(Optional.of(user));
        PasswordEncoder encoder = org.mockito.Mockito.mock(PasswordEncoder.class);
        when(encoder.encode("NewSecret123!")).thenReturn("{bcrypt}newhash");

        new PasswordRecoveryService(users, redis, org.mockito.Mockito.mock(SmtpEmailService.class), encoder, "https://example.test/reset")
                .resetPassword("token", "NewSecret123!");

        verify(users).save(user);
        verify(redis).delete("recipe-manager:password-reset:token");
    }

    @Test
    void rejectsUnknownResetToken() {
        StringRedisTemplate redis = org.mockito.Mockito.mock(StringRedisTemplate.class);
        ValueOperations<String, String> values = org.mockito.Mockito.mock(ValueOperations.class);
        when(redis.opsForValue()).thenReturn(values);
        when(values.get(any())).thenReturn(null);

        assertThatThrownBy(() -> new PasswordRecoveryService(
                org.mockito.Mockito.mock(AppUserRepository.class), redis,
                org.mockito.Mockito.mock(SmtpEmailService.class),
                org.mockito.Mockito.mock(PasswordEncoder.class), "https://example.test/reset")
                .resetPassword("missing", "NewSecret123!"))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
