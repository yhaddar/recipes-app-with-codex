package com.recipemanager.recipe;

import java.io.Serializable;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class RecipeIngredientId implements Serializable {

    @Column(name = "recipe_id")
    private UUID recipeId;

    @Column(name = "ingredient_id")
    private UUID ingredientId;

    protected RecipeIngredientId() {
    }

    public RecipeIngredientId(UUID recipeId, UUID ingredientId) {
        this.recipeId = recipeId;
        this.ingredientId = ingredientId;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof RecipeIngredientId that)) {
            return false;
        }
        return recipeId.equals(that.recipeId) && ingredientId.equals(that.ingredientId);
    }

    @Override
    public int hashCode() {
        return 31 * recipeId.hashCode() + ingredientId.hashCode();
    }
}
