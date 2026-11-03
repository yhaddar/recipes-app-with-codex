package com.recipemanager.config;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.Test;

class LoggingConfigurationTest {

    @Test
    void configuresDailyFilesWithSevenDayRetention() throws IOException {
        try (InputStream stream = getClass().getResourceAsStream("/logback-spring.xml")) {
            assertThat(stream).isNotNull();
            String configuration = new String(stream.readAllBytes(), StandardCharsets.UTF_8);

            assertThat(configuration).contains("application-%d{yyyy-MM-dd}.log");
            assertThat(configuration).contains("<maxHistory>7</maxHistory>");
            assertThat(configuration).contains("${LOG_DIR:-logs}");
        }
    }
}
