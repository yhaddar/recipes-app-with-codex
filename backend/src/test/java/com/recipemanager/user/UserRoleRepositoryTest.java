package com.recipemanager.user;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.UUID;

import com.recipemanager.role.Role;
import com.recipemanager.role.RoleName;
import com.recipemanager.role.RoleRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@ActiveProfiles("postgres")
@Transactional
class UserRoleRepositoryTest {

    @Autowired
    private AppUserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRoleRepository userRoleRepository;

    @Test
    void persistsUserRoleRelationship() {
        AppUser user = userRepository.saveAndFlush(
                new AppUser(UUID.randomUUID(), UUID.randomUUID() + "@example.test", "Ada", "Lovelace"));
        Role role = roleRepository.findByName(RoleName.USER)
                .orElseGet(() -> roleRepository.saveAndFlush(new Role(UUID.randomUUID(), RoleName.USER)));
        UserRole userRole = userRoleRepository.saveAndFlush(new UserRole(user, role));

        UserRole stored = userRoleRepository.findById(userRole.getId()).orElseThrow();

        assertThat(stored.getUser().getId()).isEqualTo(user.getId());
        assertThat(stored.getRole().getName()).isEqualTo(RoleName.USER);

        userRoleRepository.deleteById(userRole.getId());
        userRepository.deleteById(user.getId());
    }
}
