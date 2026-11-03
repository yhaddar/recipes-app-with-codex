package com.recipemanager.auth;

import java.util.UUID;

import com.recipemanager.recipe.ResourceNotFoundException;
import com.recipemanager.user.AppUser;
import com.recipemanager.user.AppUserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AdminUserService {

    private final AppUserRepository users;

    public AdminUserService(AppUserRepository users) {
        this.users = users;
    }

    @Transactional
    public AccountStatusResponse deactivate(UUID userId) {
        AppUser user = users.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User was not found"));
        user.deactivate();
        users.save(user);
        return new AccountStatusResponse(user.getId(), user.getAccountStatus().name());
    }

    @Transactional
    public AccountStatusResponse activate(UUID userId) {
        AppUser user = users.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User was not found"));
        user.activate();
        users.save(user);
        return new AccountStatusResponse(user.getId(), user.getAccountStatus().name());
    }
}
