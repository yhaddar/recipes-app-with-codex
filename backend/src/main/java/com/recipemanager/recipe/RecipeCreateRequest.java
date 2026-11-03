package com.recipemanager.recipe;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record RecipeCreateRequest(
        @NotBlank @Size(max = 200) String title,
        @Size(max = 10000) String description,
        @NotNull @Min(0) Integer totalTimeMinutes,
        @NotNull @Min(1) Integer servings,
        @NotNull Difficulty difficulty) {
}
