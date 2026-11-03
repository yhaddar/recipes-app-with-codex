package com.recipemanager.auth;

import java.time.Duration;
import java.util.UUID;

import com.recipemanager.email.EmailMessage;
import com.recipemanager.email.SmtpEmailService;
import com.recipemanager.user.AppUser;
import com.recipemanager.user.AppUserRepository;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EmailVerificationService {

    private static final String KEY_PREFIX = "recipe-manager:email-verification:";
    private static final Duration TOKEN_TTL = Duration.ofHours(24);

    private final StringRedisTemplate redis;
    private final SmtpEmailService mail;
    private final AppUserRepository users;
    private final String verificationUrl;

    public EmailVerificationService(
            StringRedisTemplate redis,
            SmtpEmailService mail,
            AppUserRepository users,
            @Value("${app.auth.verification-url}")
            String verificationUrl) {
        this.redis = redis;
        this.mail = mail;
        this.users = users;
        this.verificationUrl = verificationUrl;
    }

    public String issueToken(UUID userId, String email) {
        String token = UUID.randomUUID().toString();
        redis.opsForValue().set(KEY_PREFIX + token, userId.toString(), TOKEN_TTL);
        mail.send(new EmailMessage(
                email,
                "Verify your Recipe Manager account",
                "Verify your account: " + verificationUrl + "?token=" + token));
        return token;
    }

    @Transactional
    public void verify(String token) {
        if (token == null || token.isBlank()) {
            throw new IllegalArgumentException("Verification token is required");
        }
        String userId = redis.opsForValue().get(KEY_PREFIX + token);
        if (userId == null) {
            throw new IllegalArgumentException("Verification token is invalid or expired");
        }
        AppUser user = users.findById(UUID.fromString(userId))
                .orElseThrow(() -> new IllegalArgumentException("Verification account was not found"));
        user.verifyEmail();
        users.save(user);
        redis.delete(KEY_PREFIX + token);
    }
}
