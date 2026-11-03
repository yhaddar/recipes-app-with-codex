package com.recipemanager.auth;

import java.util.UUID;

public record RegistrationResponse(UUID id, String email, String accountStatus) {
}
