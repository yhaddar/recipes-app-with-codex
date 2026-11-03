package com.recipemanager.recipe;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.UUID;

import com.recipemanager.ingredient.Ingredient;
import com.recipemanager.ingredient.IngredientRepository;
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
class RecipeIngredientRepositoryTest {

    @Autowired
    private AppUserRepository userRepository;

    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private IngredientRepository ingredientRepository;

    @Autowired
    private RecipeIngredientRepository repository;

    @Test
    void persistsQuantityAndOrderForRecipeIngredient() {
        AppUser owner = userRepository.saveAndFlush(new AppUser(
                UUID.randomUUID(), UUID.randomUUID() + "@example.test", "Ada", "Lovelace"));
        Recipe recipe = recipeRepository.saveAndFlush(
                new Recipe(UUID.randomUUID(), owner, "Test Recipe", 2));
        Ingredient ingredient = ingredientRepository.saveAndFlush(
                new Ingredient(UUID.randomUUID(), UUID.randomUUID() + " flour"));

        RecipeIngredient recipeIngredient = repository.saveAndFlush(
                new RecipeIngredient(recipe, ingredient, new BigDecimal("250.000"), "g", 0));

        RecipeIngredient stored = repository.findById(
                new RecipeIngredientId(recipe.getId(), ingredient.getId())).orElseThrow();

        assertThat(stored.getQuantity()).isEqualByComparingTo("250");
        assertThat(stored.getDisplayOrder()).isZero();
        assertThat(stored.getIngredient().getId()).isEqualTo(ingredient.getId());
        assertThat(recipeIngredient).isNotNull();
    }
}
