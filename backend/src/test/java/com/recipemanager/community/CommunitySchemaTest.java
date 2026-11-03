package com.recipemanager.community;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@ActiveProfiles("postgres")
@Transactional
class CommunitySchemaTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void createsCommunityTables() {
        Integer tableCount = jdbcTemplate.queryForObject("""
                SELECT COUNT(*) FROM information_schema.tables
                WHERE table_schema = 'public'
                  AND table_name IN ('recipe_comments', 'recipe_likes', 'recipe_favorites',
                                     'blog_comments', 'blog_likes')
                """, Integer.class);

        assertThat(tableCount).isEqualTo(5);
    }

    @Test
    void preventsDuplicateRecipeLikeForSameUser() {
        UUID userId = UUID.randomUUID();
        UUID recipeId = UUID.randomUUID();
        jdbcTemplate.update("INSERT INTO users (id, email, first_name, last_name) VALUES (?, ?, ?, ?)",
                userId, userId + "@example.test", "Ada", "Lovelace");
        jdbcTemplate.update("INSERT INTO recipes (id, owner_id, title, servings) VALUES (?, ?, ?, ?)",
                recipeId, userId, "Community Recipe", 2);
        jdbcTemplate.update("INSERT INTO recipe_likes (recipe_id, user_id) VALUES (?, ?)", recipeId, userId);

        assertThatThrownBy(() -> jdbcTemplate.update(
                "INSERT INTO recipe_likes (recipe_id, user_id) VALUES (?, ?)", recipeId, userId))
                .isInstanceOf(DataIntegrityViolationException.class);
    }
}
