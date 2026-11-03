package com.recipemanager.mapper;

import java.util.Objects;

import com.recipemanager.dto.response.UserSummaryResponse;
import com.recipemanager.user.AppUser;

public class UserSummaryMapper implements DtoMapper<AppUser, UserSummaryResponse> {

    @Override
    public UserSummaryResponse toResponse(AppUser source) {
        AppUser user = Objects.requireNonNull(source, "source must not be null");
        return new UserSummaryResponse(
                user.getId(),
                user.getEmail(),
                user.getAccountStatus().name());
    }
}
