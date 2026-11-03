package com.recipemanager.recipe;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RecipeImageRepository extends JpaRepository<RecipeImage, UUID> {
}
