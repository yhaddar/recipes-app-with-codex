package com.recipemanager.recipe;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RecipeInstructionRepository extends JpaRepository<RecipeInstruction, RecipeInstructionId> {
}
