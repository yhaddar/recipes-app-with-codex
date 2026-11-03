package com.recipemanager.recipe;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.UUID;

import com.recipemanager.user.AppUser;
import com.recipemanager.user.AppUserRepository;
import org.junit.jupiter.api.Test;

class RecipeServiceTest {

    @Test
    void createsDraftOwnedByAuthenticatedUser() {
        RecipeRepository recipes = org.mockito.Mockito.mock(RecipeRepository.class);
        AppUserRepository users = org.mockito.Mockito.mock(AppUserRepository.class);
        UUID ownerId = UUID.randomUUID();
        AppUser owner = new AppUser(ownerId, "cook@example.com", "Ada", "Cook");
        when(users.findById(ownerId)).thenReturn(Optional.of(owner));

        RecipeResponse response = new RecipeService(recipes, users).create(ownerId,
                new RecipeCreateRequest("Pasta", "Simple pasta", 30, 2, Difficulty.EASY));

        assertThat(response.title()).isEqualTo("Pasta");
        assertThat(response.publicationStatus()).isEqualTo(PublicationStatus.DRAFT.name());
        assertThat(response.ownerId()).isEqualTo(ownerId);
        verify(recipes).save(any(Recipe.class));
    }

    @Test
    void rejectsUnknownOwner() {
        RecipeRepository recipes = org.mockito.Mockito.mock(RecipeRepository.class);
        AppUserRepository users = org.mockito.Mockito.mock(AppUserRepository.class);
        when(users.findById(any())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> new RecipeService(recipes, users).create(
                UUID.randomUUID(), new RecipeCreateRequest("Pasta", null, 30, 2, Difficulty.EASY)))
                .isInstanceOf(ResourceNotFoundException.class);
    }
}
