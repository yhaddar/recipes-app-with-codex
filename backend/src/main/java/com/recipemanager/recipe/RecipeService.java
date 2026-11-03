package com.recipemanager.recipe;

import java.util.UUID;

import com.recipemanager.user.AppUser;
import com.recipemanager.user.AppUserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RecipeService {

    private final RecipeRepository recipes;
    private final AppUserRepository users;

    public RecipeService(RecipeRepository recipes, AppUserRepository users) {
        this.recipes = recipes;
        this.users = users;
    }

    @Transactional
    public RecipeResponse create(UUID ownerId, RecipeCreateRequest request) {
        AppUser owner = users.findById(ownerId)
                .orElseThrow(() -> new ResourceNotFoundException("Recipe owner was not found"));
        Recipe recipe = new Recipe(UUID.randomUUID(), owner, request.title().trim(), request.servings());
        recipe.updateDetails(request.description(), request.totalTimeMinutes(), request.difficulty());
        recipes.save(recipe);
        return toResponse(recipe);
    }

    private RecipeResponse toResponse(Recipe recipe) {
        return new RecipeResponse(
                recipe.getId(),
                recipe.getOwner().getId(),
                recipe.getTitle(),
                recipe.getPublicationStatus().name());
    }
}
