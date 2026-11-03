package com.recipemanager.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("postgres")
class AppUserRepositoryTest {

    @Autowired
    private AppUserRepository repository;

    @Test
    void persistsUserWithPendingAccountStatus() {
        AppUser user = new AppUser(UUID.randomUUID(), uniqueEmail(), "Ada", "Lovelace");

        AppUser saved = repository.saveAndFlush(user);

        assertThat(saved.getId()).isEqualTo(user.getId());
        assertThat(saved.getAccountStatus()).isEqualTo(AccountStatus.PENDING);
        repository.deleteById(saved.getId());
    }

    @Test
    void rejectsDuplicateEmail() {
        String email = uniqueEmail();
        AppUser first = repository.saveAndFlush(new AppUser(UUID.randomUUID(), email, "Ada", "Lovelace"));

        assertThatThrownBy(() -> repository.saveAndFlush(
                new AppUser(UUID.randomUUID(), email, "Grace", "Hopper")))
                .isInstanceOf(DataIntegrityViolationException.class);

        repository.deleteById(first.getId());
    }

    private String uniqueEmail() {
        return UUID.randomUUID() + "@example.test";
    }
}
