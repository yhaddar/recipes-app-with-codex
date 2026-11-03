package com.recipemanager.dto.response;

import java.util.UUID;

public record UserSummaryResponse(
        UUID id,
        String email,
        String accountStatus) {
}
