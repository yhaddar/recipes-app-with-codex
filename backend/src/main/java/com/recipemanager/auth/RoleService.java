package com.recipemanager.auth;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import com.recipemanager.role.RoleName;
import com.recipemanager.user.UserRoleRepository;
import org.springframework.stereotype.Service;

@Service
public class RoleService {

    private final UserRoleRepository userRoles;

    public RoleService(UserRoleRepository userRoles) {
        this.userRoles = userRoles;
    }

    public Set<RoleName> roleNamesFor(UUID userId) {
        return userRoles.findByIdUserId(userId).stream()
                .map(userRole -> userRole.getRole().getName())
                .collect(Collectors.toUnmodifiableSet());
    }
}
