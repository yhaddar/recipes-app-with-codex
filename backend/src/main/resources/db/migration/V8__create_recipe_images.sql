CREATE TABLE recipe_images (
    id UUID NOT NULL,
    recipe_id UUID NOT NULL,
    storage_path VARCHAR(1000) NOT NULL,
    original_filename VARCHAR(255) NOT NULL,
    content_type VARCHAR(100) NOT NULL,
    file_size_bytes BIGINT NOT NULL,
    display_order INTEGER NOT NULL DEFAULT 0,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT pk_recipe_images PRIMARY KEY (id),
    CONSTRAINT fk_recipe_images_recipe FOREIGN KEY (recipe_id) REFERENCES recipes (id) ON DELETE CASCADE,
    CONSTRAINT ck_recipe_images_file_size CHECK (file_size_bytes > 0),
    CONSTRAINT ck_recipe_images_display_order CHECK (display_order >= 0)
);
