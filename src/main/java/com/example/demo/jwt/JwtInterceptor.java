package com.example.demo.jwt;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.servlet.HandlerInterceptor;

@RequiredArgsConstructor
public class JwtInterceptor implements HandlerInterceptor {
    private final JwtService jwtService;
    private final TokenBlackList tokenBlacklist;



    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler,String token) throws Exception {
        String username = jwtService.extractUsername(token); // Implement this method according to your token extraction logic
//        String token = jwtService.extractUsername(request); // Implement this method according to your token extraction logic

        if (token != null && jwtService.isTokenValid(token,null) && !tokenBlacklist.isTokenInvalidated(token)) {
            // Token is valid and not invalidated, proceed with request handling
            return true;
        } else {
            // Invalid or invalidated token, return unauthorized response
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid or expired token");
            return false;
        }
    }
}
