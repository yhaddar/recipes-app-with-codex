CREATE UNIQUE INDEX uk_users_email_lower ON users (LOWER(email));

CREATE INDEX ix_user_roles_role_id ON user_roles (role_id);
CREATE INDEX ix_recipes_owner_id ON recipes (owner_id);
CREATE INDEX ix_recipes_publication_status ON recipes (publication_status);
CREATE INDEX ix_recipe_ingredients_ingredient_id ON recipe_ingredients (ingredient_id);
CREATE INDEX ix_recipe_images_recipe_id ON recipe_images (recipe_id, display_order);
CREATE INDEX ix_blog_posts_author_id ON blog_posts (author_id);
CREATE INDEX ix_blog_posts_publication_status ON blog_posts (publication_status);
CREATE INDEX ix_tasks_owner_id ON tasks (owner_id);
CREATE INDEX ix_tasks_owner_due_date ON tasks (owner_id, due_date);
CREATE INDEX ix_recipe_comments_recipe_id ON recipe_comments (recipe_id, created_at);
CREATE INDEX ix_blog_comments_blog_post_id ON blog_comments (blog_post_id, created_at);
CREATE INDEX ix_subscriptions_user_id ON subscriptions (user_id, status);
