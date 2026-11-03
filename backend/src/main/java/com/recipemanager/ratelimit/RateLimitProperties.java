package com.recipemanager.ratelimit;

import java.time.Duration;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.rate-limit")
public record RateLimitProperties(
        int limit,
        Duration window,
        String keyPrefix) {
}
