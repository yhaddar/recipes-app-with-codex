package com.recipemanager.auth;

import java.util.UUID;

import com.recipemanager.recipe.ResourceNotFoundException;
import com.recipemanager.user.AppUser;
import com.recipemanager.user.AppUserRepository;
import org.springframework.stereotype.Service;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AdminUserService {

    private final AppUserRepository users;
    private final StringRedisTemplate redis;

    public AdminUserService(AppUserRepository users, StringRedisTemplate redis) {
        this.users = users;
        this.redis = redis;
    }

    @Transactional
    public AccountStatusResponse deactivate(UUID userId) {
        AppUser user = users.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User was not found"));
        user.deactivate();
        users.save(user);
        redis.opsForValue().increment(LoginService.SESSION_VERSION_PREFIX + user.getId());
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
