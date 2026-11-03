package com.recipemanager.auth;

import java.time.Duration;
import java.util.Locale;
import java.util.UUID;

import com.recipemanager.user.AccountStatus;
import com.recipemanager.user.AppUser;
import com.recipemanager.user.AppUserRepository;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class LoginService {

    private static final String SESSION_PREFIX = "recipe-manager:session:";
    static final String SESSION_VERSION_PREFIX = "recipe-manager:session-version:";
    private static final RedisScript<String> INITIALIZE_VERSION = new DefaultRedisScript<>(
            "local version = redis.call('GET', KEYS[1]) "
                    + "if not version then "
                    + "local initialized = redis.call('SETNX', KEYS[1], '1') "
                    + "if initialized == 1 then version = '1' else version = redis.call('GET', KEYS[1]) end "
                    + "end return version",
            String.class);
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
        String versionKey = SESSION_VERSION_PREFIX + user.getId();
        String version = redis.execute(INITIALIZE_VERSION, java.util.List.of(versionKey));
        if (version == null) {
            throw new IllegalStateException("Session version could not be initialized");
        }
        redis.opsForValue().set(SESSION_PREFIX + token, user.getId() + ":" + version, SESSION_TTL);
        return new LoginResponse(user.getId(), token, user.getAccountStatus().name());
    }
}
