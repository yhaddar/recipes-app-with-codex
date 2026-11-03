package com.recipemanager.recipe;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.UUID;

import com.recipemanager.user.AppUser;
import com.recipemanager.user.AppUserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@ActiveProfiles("postgres")
@Transactional
class RecipeImageRepositoryTest {

    @Autowired
    private AppUserRepository userRepository;

    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private RecipeImageRepository repository;

    @Test
    void persistsImageMetadataForRecipe() {
        AppUser owner = userRepository.saveAndFlush(new AppUser(
                UUID.randomUUID(), UUID.randomUUID() + "@example.test", "Ada", "Lovelace"));
        Recipe recipe = recipeRepository.saveAndFlush(
                new Recipe(UUID.randomUUID(), owner, "Test Recipe", 2));

        RecipeImage image = repository.saveAndFlush(new RecipeImage(
                UUID.randomUUID(), recipe, "storage/images/recipe/image.png", "image.png", "image/png", 2048, 0));

        RecipeImage stored = repository.findById(image.getId()).orElseThrow();
        assertThat(stored.getStoragePath()).isEqualTo("storage/images/recipe/image.png");
        assertThat(stored.getDisplayOrder()).isZero();
    }
}
