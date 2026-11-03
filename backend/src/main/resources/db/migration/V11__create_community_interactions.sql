CREATE TABLE recipe_comments (
    id UUID NOT NULL,
    recipe_id UUID NOT NULL,
    author_id UUID NOT NULL,
    parent_comment_id UUID,
    content TEXT NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT pk_recipe_comments PRIMARY KEY (id),
    CONSTRAINT fk_recipe_comments_recipe FOREIGN KEY (recipe_id) REFERENCES recipes (id) ON DELETE CASCADE,
    CONSTRAINT fk_recipe_comments_author FOREIGN KEY (author_id) REFERENCES users (id) ON DELETE CASCADE,
    CONSTRAINT fk_recipe_comments_parent FOREIGN KEY (parent_comment_id) REFERENCES recipe_comments (id) ON DELETE SET NULL
);

CREATE TABLE recipe_likes (
    recipe_id UUID NOT NULL,
    user_id UUID NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT pk_recipe_likes PRIMARY KEY (recipe_id, user_id),
    CONSTRAINT fk_recipe_likes_recipe FOREIGN KEY (recipe_id) REFERENCES recipes (id) ON DELETE CASCADE,
    CONSTRAINT fk_recipe_likes_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

CREATE TABLE recipe_favorites (
    recipe_id UUID NOT NULL,
    user_id UUID NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT pk_recipe_favorites PRIMARY KEY (recipe_id, user_id),
    CONSTRAINT fk_recipe_favorites_recipe FOREIGN KEY (recipe_id) REFERENCES recipes (id) ON DELETE CASCADE,
    CONSTRAINT fk_recipe_favorites_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

CREATE TABLE blog_comments (
    id UUID NOT NULL,
    blog_post_id UUID NOT NULL,
    author_id UUID NOT NULL,
    parent_comment_id UUID,
    content TEXT NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT pk_blog_comments PRIMARY KEY (id),
    CONSTRAINT fk_blog_comments_post FOREIGN KEY (blog_post_id) REFERENCES blog_posts (id) ON DELETE CASCADE,
    CONSTRAINT fk_blog_comments_author FOREIGN KEY (author_id) REFERENCES users (id) ON DELETE CASCADE,
    CONSTRAINT fk_blog_comments_parent FOREIGN KEY (parent_comment_id) REFERENCES blog_comments (id) ON DELETE SET NULL
);

CREATE TABLE blog_likes (
    blog_post_id UUID NOT NULL,
    user_id UUID NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT pk_blog_likes PRIMARY KEY (blog_post_id, user_id),
    CONSTRAINT fk_blog_likes_post FOREIGN KEY (blog_post_id) REFERENCES blog_posts (id) ON DELETE CASCADE,
    CONSTRAINT fk_blog_likes_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);
