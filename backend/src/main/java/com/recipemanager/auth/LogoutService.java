package com.recipemanager.auth;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class LogoutService {

    private static final String SESSION_PREFIX = "recipe-manager:session:";

    private final StringRedisTemplate redis;

    public LogoutService(StringRedisTemplate redis) {
        this.redis = redis;
    }

    public void logout(String sessionToken) {
        if (sessionToken == null || sessionToken.isBlank()) {
            throw new IllegalArgumentException("Session token is required");
        }
        redis.delete(SESSION_PREFIX + sessionToken);
    }
}
