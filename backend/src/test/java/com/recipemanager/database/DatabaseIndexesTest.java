package com.recipemanager.database;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("postgres")
class DatabaseIndexesTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void createsRequiredLookupIndexes() {
        Integer indexCount = jdbcTemplate.queryForObject("""
                SELECT COUNT(*) FROM pg_indexes
                WHERE schemaname = 'public'
                  AND indexname IN ('uk_users_email_lower', 'ix_recipes_owner_id',
                                    'ix_tasks_owner_due_date', 'ix_subscriptions_user_id')
                """, Integer.class);

        assertThat(indexCount).isEqualTo(4);
    }
}
