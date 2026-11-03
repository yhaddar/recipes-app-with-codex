package com.recipemanager.recipe;

import java.util.UUID;

public record RecipeResponse(UUID id, UUID ownerId, String title, String publicationStatus) {
}
