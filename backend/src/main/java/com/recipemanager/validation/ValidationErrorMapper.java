package com.recipemanager.validation;

import java.util.Map;
import java.util.TreeMap;

import org.springframework.validation.BindingResult;

public final class ValidationErrorMapper {

    private ValidationErrorMapper() {
    }

    public static Map<String, String> toFieldErrors(BindingResult bindingResult) {
        Map<String, String> fieldErrors = new TreeMap<>();
        bindingResult.getFieldErrors().forEach(error ->
                fieldErrors.putIfAbsent(error.getField(), error.getDefaultMessage()));
        return fieldErrors;
    }
}
