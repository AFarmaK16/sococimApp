package com.example.demo.auth;

import com.example.demo.jwt.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



//@Component
@RequiredArgsConstructor
public class RateLimitAuthenticationFilter  {

    private final int MAX_LOGIN_ATTEMPTS = 3;
    private final LoginAttemptService loginAttemptService;
    private final JwtService jwtService;
    private final long LOGIN_ATTEMPT_TIMEOUT = 1 * 60 * 1000; // 10 minutes in milliseconds

    private Map<String, List<Long>> loginAttempts = new HashMap<>();




//    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull  FilterChain filterChain) throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");
        final String ipAdress= request.getRemoteAddr();
        final  String jwt;
        final String username ;
        if (authHeader == null || !authHeader.startsWith("Bearer ")){
            filterChain.doFilter(request,response);
            return;
        }
        jwt = authHeader.substring(7);
        //will extract the username from JWT token

        username = jwtService.extractUsername(jwt);;
        String key = username + "_" + ipAdress;
        System.out.println("yagui si dh"+ipAdress);
        if (loginAttempts.containsKey(key)) {
            List<Long> attempts = loginAttempts.get(key);
            long lastAttempt = attempts.get(attempts.size() - 1);
            if (System.currentTimeMillis() - lastAttempt < LOGIN_ATTEMPT_TIMEOUT) {
              System.out.println("Too many login attempts, please try again later.");
                filterChain.doFilter(request,response);
              return;
            }


        }
        else{
            loginAttempts.put(key, new ArrayList<>());
        }
        System.out.println("LoginAttempt >>"+loginAttempts+" key >>"+key);

        // Reset login attempts if successful
        loginAttempts.remove(key);
        //check rate limit
//        if (!rateLimitAuthService.checkLimit(key,MAX_LOGIN_ATTEMPTS,LOGIN_ATTEMPT_TIMEOUT)){
//            response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
//            response.getWriter().write("Too many attempt to authenticate "+ipAdress);
//            filterChain.doFilter(request,response);
//            return;
//        }
        System.out.println("yagui si dh"+ipAdress);
    }


//    @Override
//    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
//        String username = request.getParameter("username");
//        String ip = request.getRemoteAddr();
//        String key = username + "_" + ip;
//
//        // Add login attempt to list
//        if (!loginAttempts.containsKey(key)) {
//            loginAttempts.put(key, new ArrayList<>());
//        }
//        List<Long> attempts = loginAttempts.get(key);
//        attempts.add(System.currentTimeMillis());
//
//        // Check if rate limit exceeded
//        if (attempts.size() >= MAX_LOGIN_ATTEMPTS) {
//            System.out.println("Too many login attempts, please try again later.");
//        }
//
//        super.unsuccessfulAuthentication(request, response, failed);
//    }
}