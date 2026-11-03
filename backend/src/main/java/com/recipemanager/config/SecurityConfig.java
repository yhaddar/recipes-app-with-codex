package com.recipemanager.config;

import java.util.Arrays;
import java.util.Set;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.recipemanager.ratelimit.ClientIpResolver;
import com.recipemanager.ratelimit.RateLimitFilter;
import com.recipemanager.ratelimit.RateLimitService;
import com.recipemanager.auth.SessionAuthenticationFilter;
import com.recipemanager.user.AppUserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.http.SessionCreationPolicy;

@Configuration
@EnableConfigurationProperties(com.recipemanager.ratelimit.RateLimitProperties.class)
public class SecurityConfig {

    @Bean
    PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    ClientIpResolver clientIpResolver(
            @Value("${app.rate-limit.trusted-proxies:}") String configuredProxies) {
        Set<String> trustedProxies = Arrays.stream(configuredProxies.split(","))
                .map(String::trim)
                .filter(value -> !value.isBlank())
                .collect(java.util.stream.Collectors.toUnmodifiableSet());
        return new ClientIpResolver(trustedProxies);
    }

    @Bean
    RateLimitFilter rateLimitFilter(
            RateLimitService rateLimitService,
            ClientIpResolver clientIpResolver,
            ObjectMapper objectMapper) {
        return new RateLimitFilter(rateLimitService, clientIpResolver, objectMapper);
    }

    @Bean
    SessionAuthenticationFilter sessionAuthenticationFilter(
            org.springframework.data.redis.core.StringRedisTemplate redis,
            AppUserRepository users) {
        return new SessionAuthenticationFilter(redis, users);
    }

    @Bean
    SecurityFilterChain applicationSecurity(
            HttpSecurity http,
            RateLimitFilter rateLimitFilter,
            SessionAuthenticationFilter sessionAuthenticationFilter) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .addFilterBefore(rateLimitFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(sessionAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/api/health", "/api/auth/register", "/api/auth/login", "/api/auth/verify-email", "/actuator/health", "/api-docs/**", "/swagger-ui/**", "/swagger-ui.html")
                        .permitAll()
                        .anyRequest()
                        .authenticated());

        return http.build();
    }
}
