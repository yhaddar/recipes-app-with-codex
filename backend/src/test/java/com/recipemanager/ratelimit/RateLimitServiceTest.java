package com.recipemanager.ratelimit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.time.Duration;

import org.junit.jupiter.api.Test;
import org.springframework.data.redis.core.StringRedisTemplate;

class RateLimitServiceTest {

    @Test
    void acceptsFiveRequestsAndRejectsTheSixth() {
        StringRedisTemplate redis = org.mockito.Mockito.mock(StringRedisTemplate.class);
        RateLimitProperties properties = new RateLimitProperties(5, Duration.ofSeconds(60), "rate-limit");
        RateLimitService service = new RedisRateLimitService(redis, properties);

        when(redis.execute(any(), eq(java.util.List.of("rate-limit:203.0.113.10")), eq("60")))
                .thenReturn(1L, 2L, 3L, 4L, 5L, 6L);

        assertThat(service.isAllowed("203.0.113.10")).isTrue();
        assertThat(service.isAllowed("203.0.113.10")).isTrue();
        assertThat(service.isAllowed("203.0.113.10")).isTrue();
        assertThat(service.isAllowed("203.0.113.10")).isTrue();
        assertThat(service.isAllowed("203.0.113.10")).isTrue();
        assertThat(service.isAllowed("203.0.113.10")).isFalse();
    }

    @Test
    void keepsDifferentClientIpsIndependent() {
        StringRedisTemplate redis = org.mockito.Mockito.mock(StringRedisTemplate.class);
        RateLimitProperties properties = new RateLimitProperties(5, Duration.ofSeconds(60), "rate-limit");
        RateLimitService service = new RedisRateLimitService(redis, properties);

        when(redis.execute(any(), any(), any())).thenReturn(1L);

        assertThat(service.isAllowed("203.0.113.10")).isTrue();
        assertThat(service.isAllowed("203.0.113.11")).isTrue();
    }
}
