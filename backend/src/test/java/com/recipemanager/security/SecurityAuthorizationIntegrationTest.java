package com.recipemanager.security;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest
@ActiveProfiles("postgres")
class SecurityAuthorizationIntegrationTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private FilterChainProxy springSecurityFilterChain;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(SecurityMockMvcConfigurers.springSecurity(springSecurityFilterChain))
                .build();
    }

    @Test
    void deniesUnauthenticatedAdminMutation() throws Exception {
        mockMvc.perform(post("/api/admin/users/{userId}/deactivate", UUID.randomUUID())
                        .with(request -> {
                            request.setRemoteAddr("198.51.100.10");
                            return request;
                        }))
                .andExpect(status().isForbidden());
    }

    @Test
    void rejectsNonAdminUserFromAdminMutation() throws Exception {
        mockMvc.perform(post("/api/admin/users/{userId}/deactivate", UUID.randomUUID())
                        .with(user("user").roles("USER"))
                        .with(request -> {
                            request.setRemoteAddr("198.51.100.11");
                            return request;
                        }))
                .andExpect(status().isForbidden());
    }
}
