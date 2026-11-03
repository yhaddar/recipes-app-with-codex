package com.recipemanager.blog;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BlogPostRepository extends JpaRepository<BlogPost, UUID> {
}
