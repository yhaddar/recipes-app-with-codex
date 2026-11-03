CREATE TABLE ingredients (
    id UUID NOT NULL,
    name VARCHAR(200) NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT pk_ingredients PRIMARY KEY (id),
    CONSTRAINT uk_ingredients_name UNIQUE (name)
);
