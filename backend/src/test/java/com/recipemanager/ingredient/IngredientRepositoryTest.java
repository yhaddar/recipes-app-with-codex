package com.recipemanager.ingredient;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@ActiveProfiles("postgres")
@Transactional
class IngredientRepositoryTest {

    @Autowired
    private IngredientRepository repository;

    @Test
    void persistsIngredientName() {
        Ingredient ingredient = repository.saveAndFlush(
                new Ingredient(UUID.randomUUID(), UUID.randomUUID() + " flour"));

        assertThat(repository.findById(ingredient.getId()).orElseThrow().getName())
                .isEqualTo(ingredient.getName());
    }

    @Test
    void rejectsDuplicateIngredientName() {
        String name = UUID.randomUUID() + " salt";
        repository.saveAndFlush(new Ingredient(UUID.randomUUID(), name));

        assertThatThrownBy(() -> repository.saveAndFlush(new Ingredient(UUID.randomUUID(), name)))
                .isInstanceOf(DataIntegrityViolationException.class);
    }
}
