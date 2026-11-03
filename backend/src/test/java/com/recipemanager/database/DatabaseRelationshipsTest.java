package com.recipemanager.database;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("postgres")
class DatabaseRelationshipsTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void verifiesExpectedForeignKeyTables() {
        List<String> tables = jdbcTemplate.queryForList("""
                SELECT DISTINCT table_name
                FROM information_schema.table_constraints
                WHERE table_schema = 'public'
                  AND constraint_type = 'FOREIGN KEY'
                ORDER BY table_name
                """, String.class);

        assertThat(tables).containsExactlyInAnyOrder(
                "blog_comments", "blog_likes", "blog_post_categories", "blog_post_tags", "blog_posts",
                "recipe_comments", "recipe_favorites", "recipe_images", "recipe_ingredients", "recipe_instructions",
                "recipe_likes", "recipes", "subscriptions", "tasks", "user_roles");
    }
}
