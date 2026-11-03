CREATE TABLE blog_posts (
    id UUID NOT NULL,
    author_id UUID NOT NULL,
    recipe_id UUID,
    title VARCHAR(200) NOT NULL,
    slug VARCHAR(220) NOT NULL,
    content TEXT NOT NULL,
    cover_image_path VARCHAR(1000),
    publication_status VARCHAR(20) NOT NULL DEFAULT 'DRAFT',
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT pk_blog_posts PRIMARY KEY (id),
    CONSTRAINT fk_blog_posts_author FOREIGN KEY (author_id) REFERENCES users (id) ON DELETE CASCADE,
    CONSTRAINT fk_blog_posts_recipe FOREIGN KEY (recipe_id) REFERENCES recipes (id) ON DELETE SET NULL,
    CONSTRAINT uk_blog_posts_slug UNIQUE (slug),
    CONSTRAINT ck_blog_posts_publication_status CHECK (publication_status IN ('DRAFT', 'PUBLISHED'))
);

CREATE TABLE blog_categories (
    id UUID NOT NULL,
    name VARCHAR(100) NOT NULL,
    CONSTRAINT pk_blog_categories PRIMARY KEY (id),
    CONSTRAINT uk_blog_categories_name UNIQUE (name)
);

CREATE TABLE blog_tags (
    id UUID NOT NULL,
    name VARCHAR(100) NOT NULL,
    CONSTRAINT pk_blog_tags PRIMARY KEY (id),
    CONSTRAINT uk_blog_tags_name UNIQUE (name)
);

CREATE TABLE blog_post_categories (
    blog_post_id UUID NOT NULL,
    category_id UUID NOT NULL,
    CONSTRAINT pk_blog_post_categories PRIMARY KEY (blog_post_id, category_id),
    CONSTRAINT fk_blog_post_categories_post FOREIGN KEY (blog_post_id) REFERENCES blog_posts (id) ON DELETE CASCADE,
    CONSTRAINT fk_blog_post_categories_category FOREIGN KEY (category_id) REFERENCES blog_categories (id) ON DELETE CASCADE
);

CREATE TABLE blog_post_tags (
    blog_post_id UUID NOT NULL,
    tag_id UUID NOT NULL,
    CONSTRAINT pk_blog_post_tags PRIMARY KEY (blog_post_id, tag_id),
    CONSTRAINT fk_blog_post_tags_post FOREIGN KEY (blog_post_id) REFERENCES blog_posts (id) ON DELETE CASCADE,
    CONSTRAINT fk_blog_post_tags_tag FOREIGN KEY (tag_id) REFERENCES blog_tags (id) ON DELETE CASCADE
);
