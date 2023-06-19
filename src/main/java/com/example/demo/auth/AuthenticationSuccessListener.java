package com.example.demo.auth;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutionException;

@Component
@RequiredArgsConstructor
public class AuthenticationSuccessListener    implements ApplicationListener<AuthenticationSuccessEvent>
{
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private LoginAttemptService loginAttemptService;



    @Override @EventListener
    public void onApplicationEvent(AuthenticationSuccessEvent event) {
        final String xfHeader = request.getHeader("X-Forwarded-For");
        if (xfHeader == null || xfHeader.isEmpty() || !xfHeader.contains(request.getRemoteAddr())){
            // Check if the user's IP is blocked, regardless of successful login
            try {
                if (loginAttemptService.isBlocked()) {
                    // Handle blocking action, e.g., redirect to a blocked page or log the action
                    System.out.println("User is blocked due to exceeding login attempts.");
                    return; // Stop further processing if user is blocked
                }
            } catch (ExecutionException e) {
                throw new RuntimeException(e);
            }

            try {
                loginAttemptService.ecvictUserFromAttemptCache();
            } catch (ExecutionException e) {
                throw new RuntimeException(e);
            }
            System.out.println("Connected successfully 😉");
        }
    }
}
