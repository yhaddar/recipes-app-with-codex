package com.recipemanager.config;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class EnvironmentProfileTest {

    @Autowired
    private Environment environment;

    @Test
    void usesLocalProfileByDefault() {
        assertThat(environment.getActiveProfiles()).containsExactly("local");
    }
}
