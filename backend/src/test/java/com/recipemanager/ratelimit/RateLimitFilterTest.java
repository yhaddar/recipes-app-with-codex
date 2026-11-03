package com.recipemanager.ratelimit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import tools.jackson.databind.json.JsonMapper;
import jakarta.servlet.FilterChain;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

class RateLimitFilterTest {

    @Test
    void rejectsRequestsAfterLimitWithoutCallingApplication() throws Exception {
        RateLimitService service = mock(RateLimitService.class);
        when(service.isAllowed("198.51.100.20")).thenReturn(false);
        FilterChain chain = mock(FilterChain.class);
        RateLimitFilter filter = new RateLimitFilter(
                service,
                new ClientIpResolver(java.util.Set.of()),
                new JsonMapper());
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRemoteAddr("198.51.100.20");
        MockHttpServletResponse response = new MockHttpServletResponse();

        filter.doFilter(request, response, chain);

        assertThat(response.getStatus()).isEqualTo(429);
        assertThat(response.getContentAsString()).contains("Too many requests");
        verify(chain, never()).doFilter(request, response);
    }

    @Test
    void returnsServiceUnavailableWhenRateLimitStateIsUnavailable() throws Exception {
        RateLimitService service = mock(RateLimitService.class);
        when(service.isAllowed("198.51.100.20")).thenThrow(new RuntimeException("redis unavailable"));
        FilterChain chain = mock(FilterChain.class);
        RateLimitFilter filter = new RateLimitFilter(
                service,
                new ClientIpResolver(java.util.Set.of()),
                new JsonMapper());
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRemoteAddr("198.51.100.20");
        MockHttpServletResponse response = new MockHttpServletResponse();

        filter.doFilter(request, response, chain);

        assertThat(response.getStatus()).isEqualTo(503);
        assertThat(response.getContentAsString()).contains("Rate limiting is temporarily unavailable.");
        verify(chain, never()).doFilter(request, response);
    }
}
