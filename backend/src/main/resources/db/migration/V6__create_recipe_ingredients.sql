CREATE TABLE recipe_ingredients (
    recipe_id UUID NOT NULL,
    ingredient_id UUID NOT NULL,
    quantity NUMERIC(12, 3) NOT NULL,
    unit VARCHAR(50) NOT NULL,
    display_order INTEGER NOT NULL,
    notes VARCHAR(500),
    CONSTRAINT pk_recipe_ingredients PRIMARY KEY (recipe_id, ingredient_id),
    CONSTRAINT fk_recipe_ingredients_recipe FOREIGN KEY (recipe_id) REFERENCES recipes (id) ON DELETE CASCADE,
    CONSTRAINT fk_recipe_ingredients_ingredient FOREIGN KEY (ingredient_id) REFERENCES ingredients (id) ON DELETE RESTRICT,
    CONSTRAINT ck_recipe_ingredients_quantity CHECK (quantity > 0),
    CONSTRAINT ck_recipe_ingredients_display_order CHECK (display_order >= 0)
);
