package com.recipemanager.auth;

import java.time.Duration;
import java.util.Locale;
import java.util.UUID;

import com.recipemanager.user.AccountStatus;
import com.recipemanager.user.AppUser;
import com.recipemanager.user.AppUserRepository;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class LoginService {

    private static final String SESSION_PREFIX = "recipe-manager:session:";
    private static final Duration SESSION_TTL = Duration.ofHours(8);

    private final AppUserRepository users;
    private final PasswordEncoder passwordEncoder;
    private final StringRedisTemplate redis;

    public LoginService(AppUserRepository users, PasswordEncoder passwordEncoder, StringRedisTemplate redis) {
        this.users = users;
        this.passwordEncoder = passwordEncoder;
        this.redis = redis;
    }

    public LoginResponse login(LoginRequest request) {
        String email = request.email().trim().toLowerCase(Locale.ROOT);
        AppUser user = users.findByEmailIgnoreCase(email)
                .orElseThrow(InvalidCredentialsException::new);
        if (user.getAccountStatus() != AccountStatus.ACTIVE
                || !passwordEncoder.matches(request.password(), user.getPasswordHash())) {
            throw new InvalidCredentialsException();
        }

        String token = UUID.randomUUID().toString();
        redis.opsForValue().set(SESSION_PREFIX + token, user.getId().toString(), SESSION_TTL);
        return new LoginResponse(user.getId(), token, user.getAccountStatus().name());
    }
}
