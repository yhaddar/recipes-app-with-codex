package com.recipemanager.auth;

import java.util.UUID;

public record LoginResponse(UUID userId, String sessionToken, String accountStatus) {
}
