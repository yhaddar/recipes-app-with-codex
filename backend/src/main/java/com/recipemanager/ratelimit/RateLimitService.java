package com.recipemanager.ratelimit;

public interface RateLimitService {

    boolean isAllowed(String clientIp);
}
