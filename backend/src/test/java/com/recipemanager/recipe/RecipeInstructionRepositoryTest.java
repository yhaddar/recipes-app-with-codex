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
class RecipeInstructionRepositoryTest {

    @Autowired
    private AppUserRepository userRepository;

    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private RecipeInstructionRepository repository;

    @Test
    void persistsOrderedInstruction() {
        AppUser owner = userRepository.saveAndFlush(new AppUser(
                UUID.randomUUID(), UUID.randomUUID() + "@example.test", "Ada", "Lovelace"));
        Recipe recipe = recipeRepository.saveAndFlush(
                new Recipe(UUID.randomUUID(), owner, "Test Recipe", 2));

        RecipeInstruction instruction = repository.saveAndFlush(
                new RecipeInstruction(recipe, 0, "Mix the ingredients."));

        assertThat(repository.findById(new RecipeInstructionId(recipe.getId(), 0)).orElseThrow()
                .getInstruction()).isEqualTo("Mix the ingredients.");
        assertThat(instruction).isNotNull();
    }
}
