package com.recipemanager.auth;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.UUID;

import com.recipemanager.user.AppUser;
import com.recipemanager.user.AppUserRepository;
import org.junit.jupiter.api.Test;

class AdminUserServiceTest {

    @Test
    void deactivatesAccountAndReturnsCurrentStatus() {
        AppUserRepository users = org.mockito.Mockito.mock(AppUserRepository.class);
        UUID id = UUID.randomUUID();
        AppUser user = new AppUser(id, "cook@example.com", "Ada", "Cook");
        user.verifyEmail();
        when(users.findById(id)).thenReturn(Optional.of(user));

        AccountStatusResponse response = new AdminUserService(users).deactivate(id);

        assertThat(response.accountStatus()).isEqualTo("INACTIVE");
        verify(users).save(any(AppUser.class));
    }

    @Test
    void activatesInactiveAccountAndReturnsCurrentStatus() {
        AppUserRepository users = org.mockito.Mockito.mock(AppUserRepository.class);
        UUID id = UUID.randomUUID();
        AppUser user = new AppUser(id, "cook@example.com", "Ada", "Cook");
        user.verifyEmail();
        user.deactivate();
        when(users.findById(id)).thenReturn(Optional.of(user));

        AccountStatusResponse response = new AdminUserService(users).activate(id);

        assertThat(response.accountStatus()).isEqualTo("ACTIVE");
        verify(users).save(any(AppUser.class));
    }
}
