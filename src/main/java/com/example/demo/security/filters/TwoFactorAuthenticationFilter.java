package com.example.demo.security.filters;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.naming.AuthenticationException;
import java.io.IOException;

public class TwoFactorAuthenticationFilter extends OncePerRequestFilter {

    private final AuthenticationManager authenticationManager;
    private final TwilioVerificationService twilioVerificationService;

    public TwoFactorAuthenticationFilter(AuthenticationManager authenticationManager, TwilioVerificationService twilioVerificationService) {
        this.authenticationManager = authenticationManager;
        this.twilioVerificationService = twilioVerificationService;
    }


    private boolean requiresTwoFactorAuthentication(HttpServletRequest request) {
        // Determine if the request requires 2FA verification based on your application's logic
        // For example, you can check the endpoint or request parameters

        // Return true if 2FA verification is required, otherwise false
        return true /* your condition */;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (requiresTwoFactorAuthentication(request)) {
            // Perform the 2FA verification
            String phoneNumber = "77";/* Extract the phone number from the request */;
            String verificationCode ="68" /* Extract the verification code from the request */;

            if (1==2) {
//                if (twilioVerificationService.verifyUserTOTP(phoneNumber, verificationCode)) {
                // Verification successful, continue with authentication
                Authentication authentication = null;
                        //                    Authentication authentication = new UsernamePasswordAuthenticationToken(/* Provide the authenticated principal */, /* Provide the authenticated credentials */);
                authentication = authenticationManager.authenticate(authentication);

                SecurityContextHolder.getContext().setAuthentication(authentication);
            } else {
                // Verification failed, handle accordingly
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }
        }

        filterChain.doFilter(request, response);
    }
}
