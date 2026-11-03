package com.recipemanager.auth;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.UUID;

import com.recipemanager.role.Role;
import com.recipemanager.role.RoleName;
import com.recipemanager.role.RoleRepository;
import com.recipemanager.user.AppUser;
import com.recipemanager.user.AppUserRepository;
import com.recipemanager.user.UserRoleRepository;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.security.crypto.password.PasswordEncoder;

class RegistrationServiceTest {

    private final AppUserRepository users = org.mockito.Mockito.mock(AppUserRepository.class);
    private final RoleRepository roles = org.mockito.Mockito.mock(RoleRepository.class);
    private final UserRoleRepository userRoles = org.mockito.Mockito.mock(UserRoleRepository.class);
    private final PasswordEncoder passwordEncoder = org.mockito.Mockito.mock(PasswordEncoder.class);
    private final RegistrationService service = new RegistrationService(users, roles, userRoles, passwordEncoder);

    @Test
    void registersPendingUserWithHashedPasswordAndUserRole() {
        Role userRole = new Role(UUID.randomUUID(), RoleName.USER);
        when(users.findByEmailIgnoreCase("cook@example.com")).thenReturn(Optional.empty());
        when(roles.findByName(RoleName.USER)).thenReturn(Optional.of(userRole));
        when(passwordEncoder.encode("Secret123!")) .thenReturn("{bcrypt}hash");

        RegistrationResponse response = service.register(
                new RegistrationRequest(" Cook@Example.com ", "Secret123!", "Ada", "Cook"));

        assertThat(response.email()).isEqualTo("cook@example.com");
        assertThat(response.accountStatus()).isEqualTo("PENDING");
        verify(passwordEncoder).encode("Secret123!");
        verify(users).save(any(AppUser.class));
        verify(userRoles).save(any());
    }

    @Test
    void rejectsDuplicateEmailBeforeEncodingOrPersisting() {
        when(users.findByEmailIgnoreCase("cook@example.com"))
                .thenReturn(Optional.of(new AppUser(UUID.randomUUID(), "cook@example.com", "Existing", "Cook")));

        assertThatThrownBy(() -> service.register(
                new RegistrationRequest("cook@example.com", "Secret123!", "Ada", "Cook")))
                .isInstanceOf(DuplicateResourceException.class);

        verify(passwordEncoder, never()).encode(any());
        verify(users, never()).save(any());
    }
}
