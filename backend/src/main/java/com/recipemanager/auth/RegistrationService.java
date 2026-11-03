package com.recipemanager.auth;

import java.util.Locale;
import java.util.UUID;

import com.recipemanager.role.RoleName;
import com.recipemanager.role.RoleRepository;
import com.recipemanager.user.AppUser;
import com.recipemanager.user.AppUserRepository;
import com.recipemanager.user.UserRole;
import com.recipemanager.user.UserRoleRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RegistrationService {

    private final AppUserRepository users;
    private final RoleRepository roles;
    private final UserRoleRepository userRoles;
    private final PasswordEncoder passwordEncoder;
    private final EmailVerificationService verificationService;

    public RegistrationService(
            AppUserRepository users,
            RoleRepository roles,
            UserRoleRepository userRoles,
            PasswordEncoder passwordEncoder,
            EmailVerificationService verificationService) {
        this.users = users;
        this.roles = roles;
        this.userRoles = userRoles;
        this.passwordEncoder = passwordEncoder;
        this.verificationService = verificationService;
    }

    @Transactional
    public RegistrationResponse register(RegistrationRequest request) {
        String email = request.email().trim().toLowerCase(Locale.ROOT);
        if (users.findByEmailIgnoreCase(email).isPresent()) {
            throw new DuplicateResourceException("An account with this email already exists");
        }

        var role = roles.findByName(RoleName.USER)
                .orElseThrow(() -> new IllegalStateException("Default user role is not configured"));
        AppUser user = new AppUser(UUID.randomUUID(), email, request.firstName().trim(), request.lastName().trim());
        user.updatePasswordHash(passwordEncoder.encode(request.password()));
        users.save(user);
        userRoles.save(new UserRole(user, role));
        verificationService.issueToken(user.getId(), user.getEmail());
        return new RegistrationResponse(user.getId(), user.getEmail(), user.getAccountStatus().name());
    }
}
