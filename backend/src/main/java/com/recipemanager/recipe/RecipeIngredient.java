package com.recipemanager.recipe;

import java.math.BigDecimal;

import com.recipemanager.ingredient.Ingredient;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;

@Entity
@Table(name = "recipe_ingredients")
public class RecipeIngredient {

    @EmbeddedId
    private RecipeIngredientId id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId("recipeId")
    @JoinColumn(name = "recipe_id", nullable = false)
    private Recipe recipe;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId("ingredientId")
    @JoinColumn(name = "ingredient_id", nullable = false)
    private Ingredient ingredient;

    @Column(nullable = false, precision = 12, scale = 3)
    private BigDecimal quantity;

    @Column(nullable = false, length = 50)
    private String unit;

    @Column(name = "display_order", nullable = false)
    private Integer displayOrder;

    @Column(length = 500)
    private String notes;

    protected RecipeIngredient() {
    }

    public RecipeIngredient(Recipe recipe, Ingredient ingredient, BigDecimal quantity, String unit, int displayOrder) {
        this.id = new RecipeIngredientId(recipe.getId(), ingredient.getId());
        this.recipe = recipe;
        this.ingredient = ingredient;
        this.quantity = quantity;
        this.unit = unit;
        this.displayOrder = displayOrder;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public Integer getDisplayOrder() {
        return displayOrder;
    }

    public Ingredient getIngredient() {
        return ingredient;
    }
}
