package com.recipemanager.recipe;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.UUID;

import com.recipemanager.user.AppUser;
import com.recipemanager.user.AppUserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@ActiveProfiles("postgres")
@Transactional
class RecipeRepositoryTest {

    @Autowired
    private AppUserRepository userRepository;

    @Autowired
    private RecipeRepository recipeRepository;

    @Test
    void persistsDraftRecipeOwnedByUser() {
        AppUser owner = userRepository.saveAndFlush(new AppUser(
                UUID.randomUUID(), UUID.randomUUID() + "@example.test", "Ada", "Lovelace"));
        Recipe recipe = recipeRepository.saveAndFlush(
                new Recipe(UUID.randomUUID(), owner, "Analytical Engine Pudding", 4));

        Recipe stored = recipeRepository.findById(recipe.getId()).orElseThrow();

        assertThat(stored.getOwner().getId()).isEqualTo(owner.getId());
        assertThat(stored.getPublicationStatus()).isEqualTo(PublicationStatus.DRAFT);
    }

    @Test
    void rejectsNonPositiveServings() {
        AppUser owner = userRepository.saveAndFlush(new AppUser(
                UUID.randomUUID(), UUID.randomUUID() + "@example.test", "Ada", "Lovelace"));

        assertThatThrownBy(() -> recipeRepository.saveAndFlush(
                new Recipe(UUID.randomUUID(), owner, "Invalid Recipe", 0)))
                .isInstanceOf(DataIntegrityViolationException.class);
    }
}
