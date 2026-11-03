package com.recipemanager.database;

import static org.assertj.core.api.Assertions.assertThat;

import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.MigrationInfo;
import org.flywaydb.core.api.MigrationState;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("postgres")
class FlywayMigrationVerificationTest {

    @Autowired
    private Flyway flyway;

    @Test
    void allResolvedMigrationsAreAppliedSuccessfully() {
        MigrationInfo[] migrations = flyway.info().all();

        assertThat(migrations).hasSize(14);
        assertThat(migrations)
                .allSatisfy(migration -> assertThat(migration.getState())
                        .isEqualTo(MigrationState.SUCCESS));
        assertThat(flyway.info().current().getVersion().getVersion()).isEqualTo("14");
    }
}
