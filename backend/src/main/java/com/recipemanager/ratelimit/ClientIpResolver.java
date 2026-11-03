package com.recipemanager.ratelimit;

import java.util.Arrays;
import java.util.Set;

import jakarta.servlet.http.HttpServletRequest;

public class ClientIpResolver {

    private final Set<String> trustedProxies;

    public ClientIpResolver(Set<String> trustedProxies) {
        this.trustedProxies = Set.copyOf(trustedProxies);
    }

    public String resolve(HttpServletRequest request) {
        String remoteAddress = request.getRemoteAddr();
        if (!trustedProxies.contains(remoteAddress)) {
            return remoteAddress;
        }

        String forwardedFor = request.getHeader("X-Forwarded-For");
        if (forwardedFor == null || forwardedFor.isBlank()) {
            return remoteAddress;
        }

        return Arrays.stream(forwardedFor.split(","))
                .map(String::trim)
                .filter(value -> !value.isBlank())
                .findFirst()
                .orElse(remoteAddress);
    }
}
