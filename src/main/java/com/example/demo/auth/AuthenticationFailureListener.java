package com.example.demo.auth;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.stereotype.Component;

@Component @RequiredArgsConstructor
public class AuthenticationFailureListener
        implements ApplicationListener<AuthenticationFailureBadCredentialsEvent>
{
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private LoginAttemptService loginAttemptService;



    @Override
    public void onApplicationEvent(AuthenticationFailureBadCredentialsEvent event) {
//Object pricipal = event.getAuthentication().getPrincipal()
        System.out.println("We in the listener");
//        System.out.println(" *** login failed for the user :\t"+event.getAuthentication().getName());
            final String xfHeader = request.getHeader("X-Forwarded-For");
            if (xfHeader == null || xfHeader.isEmpty() || !xfHeader.contains(request.getRemoteAddr())){
//                System.out.println("Header vide \t"+request.getHeaderNames());
                System.out.println(request.getRemoteAddr()+" failed from this address for "+event.getAuthentication().getName());
                loginAttemptService.loginFailed(request.getRemoteAddr());// inform the loginService of the @IP from where the authentication failed
            }
            else{
                loginAttemptService.loginFailed(xfHeader.split(",")[0]);
            }

        }


}
