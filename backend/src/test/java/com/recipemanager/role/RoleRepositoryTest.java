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
        Role role = repository.findByName(RoleName.ADMIN).orElseThrow();

        assertThat(role.getName()).isEqualTo(RoleName.ADMIN);
    }

    @Test
    void rejectsDuplicateRoleName() {
        Role first = repository.findByName(RoleName.USER).orElseThrow();

        assertThatThrownBy(() -> repository.saveAndFlush(
                new Role(UUID.randomUUID(), RoleName.USER)))
                .isInstanceOf(DataIntegrityViolationException.class);
        assertThat(first.getName()).isEqualTo(RoleName.USER);
    }
}
