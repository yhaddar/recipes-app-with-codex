package com.recipemanager.auth;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Duration;
import java.util.UUID;

import com.recipemanager.email.EmailMessage;
import com.recipemanager.email.SmtpEmailService;
import com.recipemanager.user.AppUserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

class EmailVerificationServiceTest {

    @Test
    void issuesTokenWithExpiryAndSendsVerificationEmail() {
        StringRedisTemplate redis = org.mockito.Mockito.mock(StringRedisTemplate.class);
        ValueOperations<String, String> values = org.mockito.Mockito.mock(ValueOperations.class);
        when(redis.opsForValue()).thenReturn(values);
        SmtpEmailService mail = org.mockito.Mockito.mock(SmtpEmailService.class);
        EmailVerificationService service = new EmailVerificationService(
                redis, mail, org.mockito.Mockito.mock(AppUserRepository.class), "https://example.test/verify");

        String token = service.issueToken(UUID.randomUUID(), "cook@example.com");

        assertThat(token).isNotBlank();
        verify(values).set(eq("recipe-manager:email-verification:" + token), any(), eq(Duration.ofHours(24)));
        verify(mail).send(any(EmailMessage.class));
    }

    @Test
    void rejectsUnknownToken() {
        StringRedisTemplate redis = org.mockito.Mockito.mock(StringRedisTemplate.class);
        ValueOperations<String, String> values = org.mockito.Mockito.mock(ValueOperations.class);
        when(redis.opsForValue()).thenReturn(values);
        when(values.get(any())).thenReturn(null);
        EmailVerificationService service = new EmailVerificationService(
                redis, org.mockito.Mockito.mock(SmtpEmailService.class), org.mockito.Mockito.mock(AppUserRepository.class), "https://example.test/verify");

        assertThatThrownBy(() -> service.verify("missing"))
                .isInstanceOf(IllegalArgumentException.class);
    }

}
