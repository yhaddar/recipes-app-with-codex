package com.recipemanager.role;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("postgres")
class RoleRepositoryTest {

    @Autowired
    private RoleRepository repository;

    @Test
    void persistsApprovedRoleName() {
        Role role = repository.saveAndFlush(new Role(UUID.randomUUID(), RoleName.ADMIN));

        assertThat(role.getName()).isEqualTo(RoleName.ADMIN);
        repository.deleteById(role.getId());
    }

    @Test
    void rejectsDuplicateRoleName() {
        Role first = repository.saveAndFlush(new Role(UUID.randomUUID(), RoleName.USER));

        assertThatThrownBy(() -> repository.saveAndFlush(
                new Role(UUID.randomUUID(), RoleName.USER)))
                .isInstanceOf(DataIntegrityViolationException.class);

        repository.deleteById(first.getId());
    }
}
