package com.recipemanager.ratelimit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;

class ClientIpResolverTest {

    @Test
    void usesForwardedAddressOnlyWhenRequestComesFromTrustedProxy() {
        ClientIpResolver resolver = new ClientIpResolver(java.util.Set.of("10.0.0.1"));
        HttpServletRequest request = org.mockito.Mockito.mock(HttpServletRequest.class);
        when(request.getRemoteAddr()).thenReturn("10.0.0.1");
        when(request.getHeader("X-Forwarded-For")).thenReturn("203.0.113.10, 10.0.0.1");

        assertThat(resolver.resolve(request)).isEqualTo("203.0.113.10");
    }

    @Test
    void ignoresForwardedAddressFromUntrustedClient() {
        ClientIpResolver resolver = new ClientIpResolver(java.util.Set.of("10.0.0.1"));
        HttpServletRequest request = org.mockito.Mockito.mock(HttpServletRequest.class);
        when(request.getRemoteAddr()).thenReturn("198.51.100.20");
        when(request.getHeader("X-Forwarded-For")).thenReturn("203.0.113.10");

        assertThat(resolver.resolve(request)).isEqualTo("198.51.100.20");
    }
}
