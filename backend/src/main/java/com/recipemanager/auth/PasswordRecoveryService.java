package com.recipemanager.auth;

import java.time.Duration;
import java.util.Locale;
import java.util.UUID;

import com.recipemanager.email.EmailMessage;
import com.recipemanager.email.SmtpEmailService;
import com.recipemanager.user.AppUser;
import com.recipemanager.user.AppUserRepository;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PasswordRecoveryService {

    private static final String KEY_PREFIX = "recipe-manager:password-reset:";
    private static final Duration TOKEN_TTL = Duration.ofMinutes(30);

    private final AppUserRepository users;
    private final StringRedisTemplate redis;
    private final SmtpEmailService mail;
    private final PasswordEncoder passwordEncoder;
    private final String resetUrl;

    public PasswordRecoveryService(
            AppUserRepository users,
            StringRedisTemplate redis,
            SmtpEmailService mail,
            PasswordEncoder passwordEncoder,
            @Value("${app.auth.reset-url}")
            String resetUrl) {
        this.users = users;
        this.redis = redis;
        this.mail = mail;
        this.passwordEncoder = passwordEncoder;
        this.resetUrl = resetUrl;
    }

    public String requestReset(String email) {
        String normalizedEmail = email.trim().toLowerCase(Locale.ROOT);
        return users.findByEmailIgnoreCase(normalizedEmail)
                .map(user -> issueToken(user))
                .orElse(null);
    }

    @Transactional
    public void resetPassword(String token, String newPassword) {
        if (token == null || token.isBlank()) {
            throw new IllegalArgumentException("Reset token is required");
        }
        String rawUserId = redis.opsForValue().get(KEY_PREFIX + token);
        if (rawUserId == null) {
            throw new IllegalArgumentException("Reset token is invalid or expired");
        }
        AppUser user = users.findById(UUID.fromString(rawUserId))
                .orElseThrow(() -> new IllegalArgumentException("Reset account was not found"));
        user.updatePasswordHash(passwordEncoder.encode(newPassword));
        users.save(user);
        redis.delete(KEY_PREFIX + token);
    }

    private String issueToken(AppUser user) {
        String token = UUID.randomUUID().toString();
        redis.opsForValue().set(KEY_PREFIX + token, user.getId().toString(), TOKEN_TTL);
        mail.send(new EmailMessage(
                user.getEmail(),
                "Reset your Recipe Manager password",
                "Reset your password: " + resetUrl + "?token=" + token));
        return token;
    }
}
