package com.recipemanager.config;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Instant;
import java.util.Map;

import com.recipemanager.dto.response.ApiErrorResponse;
import io.swagger.v3.oas.models.OpenAPI;
import tools.jackson.databind.json.JsonMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class OpenApiConfigTest {

    @Autowired
    private OpenAPI openApi;

    @Autowired
    private JsonMapper objectMapper;

    @Test
    void exposesRecipeManagerApiMetadata() {
        assertThat(openApi.getInfo().getTitle()).isEqualTo("Recipe Manager API");
        assertThat(openApi.getInfo().getVersion()).isEqualTo("v1");
    }

    @Test
    void productionJsonMapperSerializesApiErrors() throws Exception {
        String json = objectMapper.writeValueAsString(new ApiErrorResponse(
                Instant.EPOCH,
                503,
                "Service Unavailable",
                "Authentication is temporarily unavailable.",
                "/api/private",
                Map.of()));

        assertThat(json).contains("1970-01-01T00:00:00Z");
        assertThat(json).contains("Authentication is temporarily unavailable.");
    }
}
