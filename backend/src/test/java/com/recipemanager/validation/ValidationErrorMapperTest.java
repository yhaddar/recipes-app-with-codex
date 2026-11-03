package com.recipemanager.validation;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;

class ValidationErrorMapperTest {

    @Test
    void returnsStableFirstErrorsSortedByFieldName() {
        BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(new Object(), "request");
        bindingResult.addError(new FieldError("request", "title", "Title is required"));
        bindingResult.addError(new FieldError("request", "email", "Email is invalid"));
        bindingResult.addError(new FieldError("request", "title", "Title is too short"));

        assertThat(ValidationErrorMapper.toFieldErrors(bindingResult))
                .hasSize(2)
                .containsEntry("email", "Email is invalid")
                .containsEntry("title", "Title is required");
    }

    @Test
    void returnsEmptyMapWhenThereAreNoFieldErrors() {
        BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(new Object(), "request");

        assertThat(ValidationErrorMapper.toFieldErrors(bindingResult)).isEmpty();
    }
}
