package com.recipemanager.auth;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Set;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;

class RegistrationRequestTest {

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    void rejectsInvalidEmailAndShortPassword() {
        Set<ConstraintViolation<RegistrationRequest>> violations = validator.validate(
                new RegistrationRequest("not-an-email", "short", "", ""));

        assertThat(violations).extracting(ConstraintViolation::getPropertyPath)
                .extracting(Object::toString)
                .contains("email", "password", "firstName", "lastName");
    }
}
