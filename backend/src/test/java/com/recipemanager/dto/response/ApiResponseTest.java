package com.recipemanager.dto.response;

import static org.assertj.core.api.Assertions.assertThat;

import tools.jackson.databind.json.JsonMapper;
import org.junit.jupiter.api.Test;

class ApiResponseTest {

    private final JsonMapper objectMapper = new JsonMapper();

    @Test
    void createsSuccessfulResponseWithPayloadAndTimestamp() {
        ApiResponse<String> response = ApiResponse.of("created", "Recipe created");

        assertThat(response.success()).isTrue();
        assertThat(response.data()).isEqualTo("created");
        assertThat(response.message()).isEqualTo("Recipe created");
        assertThat(response.timestamp()).isNotNull();
    }

    @Test
    void serializesStableResponseFieldNames() throws Exception {
        String json = objectMapper.writeValueAsString(ApiResponse.of("ok"));

        assertThat(json).contains("\"success\":true");
        assertThat(json).contains("\"data\":\"ok\"");
        assertThat(json).contains("\"timestamp\"");
    }
}
