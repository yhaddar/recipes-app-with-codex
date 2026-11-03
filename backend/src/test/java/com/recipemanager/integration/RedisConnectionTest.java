package com.recipemanager.integration;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.flyway.autoconfigure.FlywayAutoConfiguration;
import org.springframework.boot.hibernate.autoconfigure.HibernateJpaAutoConfiguration;
import org.springframework.boot.jdbc.autoconfigure.DataSourceAutoConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = RedisConnectionTest.RedisOnlyApplication.class)
@ActiveProfiles("redis")
class RedisConnectionTest {

    @EnableAutoConfiguration(exclude = {
            DataSourceAutoConfiguration.class,
            FlywayAutoConfiguration.class,
            HibernateJpaAutoConfiguration.class
    })
    static class RedisOnlyApplication {
    }

    @Autowired
    private RedisConnectionFactory connectionFactory;

    @Test
    void connectsToConfiguredRedisInstance() {
        try (RedisConnection connection = connectionFactory.getConnection()) {
            assertThat(connection.ping()).isEqualTo("PONG");
        }
    }
}
