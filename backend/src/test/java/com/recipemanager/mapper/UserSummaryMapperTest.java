package com.recipemanager.mapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import java.util.UUID;

import com.recipemanager.dto.response.UserSummaryResponse;
import com.recipemanager.user.AppUser;
import org.junit.jupiter.api.Test;

class UserSummaryMapperTest {

    private final UserSummaryMapper mapper = new UserSummaryMapper();

    @Test
    void mapsOnlyPublicUserSummaryFields() {
        UUID id = UUID.randomUUID();
        AppUser user = new AppUser(id, "cook@example.com", "Ada", "Lovelace");

        UserSummaryResponse response = mapper.toResponse(user);

        assertThat(response.id()).isEqualTo(id);
        assertThat(response.email()).isEqualTo("cook@example.com");
        assertThat(response.accountStatus()).isEqualTo("PENDING");
    }

    @Test
    void mapsCollectionsWithoutExposingEntities() {
        AppUser first = new AppUser(UUID.randomUUID(), "one@example.com", "One", "Cook");
        AppUser second = new AppUser(UUID.randomUUID(), "two@example.com", "Two", "Cook");

        assertThat(mapper.toResponses(List.of(first, second)))
                .extracting(UserSummaryResponse::email)
                .containsExactly("one@example.com", "two@example.com");
    }

    @Test
    void rejectsNullSource() {
        assertThatThrownBy(() -> mapper.toResponse(null))
                .isInstanceOf(NullPointerException.class);
    }
}
