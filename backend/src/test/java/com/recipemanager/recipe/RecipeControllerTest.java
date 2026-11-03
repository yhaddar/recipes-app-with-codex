package com.recipemanager.recipe;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

class RecipeControllerTest {

    @Test
    void createsRecipeForAuthenticatedUser() throws Exception {
        RecipeService service = mock(RecipeService.class);
        UUID ownerId = UUID.randomUUID();
        RecipeResponse response = new RecipeResponse(UUID.randomUUID(), ownerId, "Pasta", "DRAFT");
        when(service.create(any(), any())).thenReturn(response);
        var result = new RecipeController(service).create(
                new RecipeCreateRequest("Pasta", "Simple pasta", 30, 2, Difficulty.EASY),
                UsernamePasswordAuthenticationToken.authenticated(ownerId.toString(), null, java.util.List.of()));

        org.assertj.core.api.Assertions.assertThat(result.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        org.assertj.core.api.Assertions.assertThat(result.getBody().data().title()).isEqualTo("Pasta");
    }
}
