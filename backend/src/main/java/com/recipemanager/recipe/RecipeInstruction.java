package com.recipemanager.recipe;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;

@Entity
@Table(name = "recipe_instructions")
public class RecipeInstruction {

    @EmbeddedId
    private RecipeInstructionId id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId("recipeId")
    @JoinColumn(name = "recipe_id", nullable = false)
    private Recipe recipe;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String instruction;

    protected RecipeInstruction() {
    }

    public RecipeInstruction(Recipe recipe, int stepOrder, String instruction) {
        this.id = new RecipeInstructionId(recipe.getId(), stepOrder);
        this.recipe = recipe;
        this.instruction = instruction;
    }

    public String getInstruction() {
        return instruction;
    }
}
