package com.recipemanager.ratelimit;

import java.util.List;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Service;

@Service
public class RedisRateLimitService implements RateLimitService {

    private static final RedisScript<Long> INCREMENT_AND_EXPIRE = new DefaultRedisScript<>(
            "local current = redis.call('INCR', KEYS[1]) "
                    + "if current == 1 then redis.call('EXPIRE', KEYS[1], ARGV[1]) end "
                    + "return current",
            Long.class);

    private final StringRedisTemplate redis;
    private final RateLimitProperties properties;

    public RedisRateLimitService(StringRedisTemplate redis, RateLimitProperties properties) {
        this.redis = redis;
        this.properties = properties;
    }

    @Override
    public boolean isAllowed(String clientIp) {
        if (clientIp == null || clientIp.isBlank()) {
            throw new IllegalArgumentException("Client IP is required");
        }

        String key = properties.keyPrefix() + ":" + clientIp;
        Long count = redis.execute(
                INCREMENT_AND_EXPIRE,
                List.of(key),
                String.valueOf(properties.window().toSeconds()));

        return count != null && count <= properties.limit();
    }
}
