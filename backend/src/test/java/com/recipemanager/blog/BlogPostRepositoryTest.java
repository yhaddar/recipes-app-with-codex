package com.recipemanager.blog;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.UUID;

import com.recipemanager.user.AppUser;
import com.recipemanager.user.AppUserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@ActiveProfiles("postgres")
@Transactional
class BlogPostRepositoryTest {

    @Autowired
    private AppUserRepository userRepository;

    @Autowired
    private BlogPostRepository repository;

    @Test
    void persistsDraftBlogPost() {
        AppUser author = userRepository.saveAndFlush(new AppUser(
                UUID.randomUUID(), UUID.randomUUID() + "@example.test", "Ada", "Lovelace"));
        BlogPost post = repository.saveAndFlush(new BlogPost(
                UUID.randomUUID(), author, "A Recipe Story", UUID.randomUUID() + "-story", "Content"));

        assertThat(repository.findById(post.getId()).orElseThrow().getPublicationStatus())
                .isEqualTo(BlogPublicationStatus.DRAFT);
    }

    @Test
    void rejectsDuplicateSlug() {
        AppUser author = userRepository.saveAndFlush(new AppUser(
                UUID.randomUUID(), UUID.randomUUID() + "@example.test", "Ada", "Lovelace"));
        String slug = UUID.randomUUID() + "-story";
        repository.saveAndFlush(new BlogPost(UUID.randomUUID(), author, "One", slug, "Content"));

        assertThatThrownBy(() -> repository.saveAndFlush(
                new BlogPost(UUID.randomUUID(), author, "Two", slug, "Content")))
                .isInstanceOf(DataIntegrityViolationException.class);
    }
}
