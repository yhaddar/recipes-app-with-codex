package com.recipemanager.auth;

import java.util.UUID;

import com.recipemanager.recipe.ResourceNotFoundException;
import com.recipemanager.user.AppUser;
import com.recipemanager.user.AppUserRepository;
import org.springframework.stereotype.Service;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AdminUserService {

    private static final RedisScript<Long> BUMP_SESSION_VERSION = new DefaultRedisScript<>(
            "local version = redis.call('INCR', KEYS[1]) "
                    + "if version == 1 then redis.call('SET', KEYS[1], '2') return 2 end "
                    + "return version",
            Long.class);

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
        bumpSessionVersion(user.getId());
        return new AccountStatusResponse(user.getId(), user.getAccountStatus().name());
    }

    @Transactional
    public AccountStatusResponse activate(UUID userId) {
        AppUser user = users.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User was not found"));
        user.activate();
        users.save(user);
        bumpSessionVersion(user.getId());
        return new AccountStatusResponse(user.getId(), user.getAccountStatus().name());
    }

    private void bumpSessionVersion(UUID userId) {
        String versionKey = LoginService.SESSION_VERSION_PREFIX + userId;
        redis.execute(BUMP_SESSION_VERSION, java.util.List.of(versionKey));
    }
}
