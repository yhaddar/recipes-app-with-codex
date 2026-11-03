package com.recipemanager.auth;

import java.util.UUID;

public record AccountStatusResponse(UUID userId, String accountStatus) {
}
