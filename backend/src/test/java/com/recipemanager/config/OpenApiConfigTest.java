package com.recipemanager.config;

import static org.assertj.core.api.Assertions.assertThat;

import io.swagger.v3.oas.models.OpenAPI;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class OpenApiConfigTest {

    @Autowired
    private OpenAPI openApi;

    @Test
    void exposesRecipeManagerApiMetadata() {
        assertThat(openApi.getInfo().getTitle()).isEqualTo("Recipe Manager API");
        assertThat(openApi.getInfo().getVersion()).isEqualTo("v1");
    }
}
