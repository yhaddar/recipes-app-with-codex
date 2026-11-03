package com.recipemanager.auth;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;

class AuthControllerTest {

    private MockMvc mockMvc;
    private RegistrationService registrationService;

    @BeforeEach
    void setUp() {
        registrationService = mock(RegistrationService.class);
        mockMvc = MockMvcBuilders.standaloneSetup(new AuthController(registrationService)).build();
    }

    @Test
    void createsRegistrationAndReturnsCommonResponseEnvelope() throws Exception {
        RegistrationResponse response = new RegistrationResponse(
                UUID.randomUUID(), "cook@example.com", "PENDING");
        when(registrationService.register(any())).thenReturn(response);

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(
                                new RegistrationRequest("cook@example.com", "Secret123!", "Ada", "Cook"))))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.email").value("cook@example.com"));
    }
}
