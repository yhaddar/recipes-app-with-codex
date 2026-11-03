package com.recipemanager.recipe;

import java.io.Serializable;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class RecipeInstructionId implements Serializable {

    @Column(name = "recipe_id")
    private UUID recipeId;

    @Column(name = "step_order")
    private Integer stepOrder;

    protected RecipeInstructionId() {
    }

    public RecipeInstructionId(UUID recipeId, int stepOrder) {
        this.recipeId = recipeId;
        this.stepOrder = stepOrder;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof RecipeInstructionId that)) {
            return false;
        }
        return recipeId.equals(that.recipeId) && stepOrder.equals(that.stepOrder);
    }

    @Override
    public int hashCode() {
        return 31 * recipeId.hashCode() + stepOrder.hashCode();
    }
}
