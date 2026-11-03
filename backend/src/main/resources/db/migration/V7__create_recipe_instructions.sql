CREATE TABLE recipe_instructions (
    recipe_id UUID NOT NULL,
    step_order INTEGER NOT NULL,
    instruction TEXT NOT NULL,
    CONSTRAINT pk_recipe_instructions PRIMARY KEY (recipe_id, step_order),
    CONSTRAINT fk_recipe_instructions_recipe FOREIGN KEY (recipe_id) REFERENCES recipes (id) ON DELETE CASCADE,
    CONSTRAINT ck_recipe_instructions_step_order CHECK (step_order >= 0)
);
