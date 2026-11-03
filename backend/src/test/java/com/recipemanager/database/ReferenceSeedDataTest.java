package com.recipemanager.database;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("postgres")
class ReferenceSeedDataTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void seedsApprovedRolesAndPlans() {
        Integer roleCount = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM roles WHERE name IN ('GUEST', 'USER', 'ADMIN')", Integer.class);
        Integer planCount = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM subscription_plans WHERE code IN ('FREE_TRIAL', 'FREE', 'PRO', 'PREMIUM')",
                Integer.class);

        assertThat(roleCount).isEqualTo(3);
        assertThat(planCount).isEqualTo(4);
    }
}
