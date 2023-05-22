package com.example.demo.auth;

import java.util.concurrent.TimeUnit;

public interface RateLimitService {
    void hit(String key, long duration, TimeUnit timeUnit);
    boolean isAllowed(String key);
}
