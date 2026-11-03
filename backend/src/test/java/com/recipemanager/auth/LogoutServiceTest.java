package com.recipemanager.auth;

import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;
import org.springframework.data.redis.core.StringRedisTemplate;

class LogoutServiceTest {

    @Test
    void deletesSessionTokenWithoutLoggingItsValue() {
        StringRedisTemplate redis = org.mockito.Mockito.mock(StringRedisTemplate.class);

        new LogoutService(redis).logout("session-token");

        verify(redis).delete("recipe-manager:session:session-token");
    }
}
