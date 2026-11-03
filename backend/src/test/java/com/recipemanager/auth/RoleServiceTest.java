package com.recipemanager.auth;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.UUID;

import com.recipemanager.role.Role;
import com.recipemanager.role.RoleName;
import com.recipemanager.user.UserRole;
import com.recipemanager.user.UserRoleRepository;
import org.junit.jupiter.api.Test;

class RoleServiceTest {

    @Test
    void returnsStableRoleNamesForUser() {
        UserRoleRepository repository = org.mockito.Mockito.mock(UserRoleRepository.class);
        UUID userId = UUID.randomUUID();
        Role user = new Role(UUID.randomUUID(), RoleName.USER);
        Role admin = new Role(UUID.randomUUID(), RoleName.ADMIN);
        when(repository.findByIdUserId(userId)).thenReturn(List.of(
                new UserRole(new com.recipemanager.user.AppUser(userId, "a@b.test", "A", "B"), user),
                new UserRole(new com.recipemanager.user.AppUser(userId, "a@b.test", "A", "B"), admin)));

        assertThat(new RoleService(repository).roleNamesFor(userId))
                .containsExactlyInAnyOrder(RoleName.USER, RoleName.ADMIN);
    }
}
