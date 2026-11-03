package com.recipemanager.database;

import static org.assertj.core.api.Assertions.assertThat;

import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("postgres")
class FlywayConfigurationTest {

    @Autowired
    private Flyway flyway;

    @Test
    void configuresFlywayForPostgresMigrations() {
        assertThat(flyway.getConfiguration().getLocations())
                .anySatisfy(location -> assertThat(location.getDescriptor())
                        .isEqualTo("classpath:db/migration"));
    }
}
