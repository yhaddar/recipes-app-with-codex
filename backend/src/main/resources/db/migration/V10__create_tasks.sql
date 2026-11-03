CREATE TABLE tasks (
    id UUID NOT NULL,
    owner_id UUID NOT NULL,
    recipe_id UUID,
    title VARCHAR(200) NOT NULL,
    description TEXT,
    priority VARCHAR(20) NOT NULL DEFAULT 'MEDIUM',
    due_date DATE,
    completed BOOLEAN NOT NULL DEFAULT FALSE,
    completed_at TIMESTAMPTZ,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT pk_tasks PRIMARY KEY (id),
    CONSTRAINT fk_tasks_owner FOREIGN KEY (owner_id) REFERENCES users (id) ON DELETE CASCADE,
    CONSTRAINT fk_tasks_recipe FOREIGN KEY (recipe_id) REFERENCES recipes (id) ON DELETE SET NULL,
    CONSTRAINT ck_tasks_priority CHECK (priority IN ('LOW', 'MEDIUM', 'HIGH')),
    CONSTRAINT ck_tasks_completion_timestamp CHECK ((completed = FALSE AND completed_at IS NULL) OR completed = TRUE)
);
