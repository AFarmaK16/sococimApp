package com.example.demo.jwt;

//import com.example.demo.auth.ApplicationUserService;

import com.example.demo.auth.ApplicationUserService;
import com.example.demo.services.AccountService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter
        extends OncePerRequestFilter
{
    private final JwtService jwtService;
    private final TokenBlackList tokenBlacklist;
//    private final AccountService accountService;
    private final ApplicationUserService applicationUserService;
//    private final ApplicationConfig applicationConfig;
//    @Override
    protected void doFilterInternal(
             @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {
        if (request.getServletPath().contains("/api/v1/auth")) {
            filterChain.doFilter(request, response);
            return;
        }
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String username;
        //Checkinf if jwt exist
        if (authHeader == null || !authHeader.startsWith("Bearer ")){
            filterChain.doFilter(request,response);
            return;
        }
        jwt = authHeader.substring(7);
        //will extract the username from JWT token
        username = jwtService.extractUsername(jwt);
try{
    if (username !=null &&
            SecurityContextHolder.getContext().getAuthentication() == null//check if user is already authenticated
    ){
        UserDetails userDetails = applicationUserService.loadUserByUsername(username);
        if (jwtService.isTokenValid(jwt,userDetails) && !tokenBlacklist.isTokenInvalidated(jwt)){
//    if (jwtService.isTokenValid(jwt,userDetails)){
            //updateSecurityContext
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                    userDetails,
                    null,
                    userDetails.getAuthorities());
            authenticationToken.setDetails(
                    new WebAuthenticationDetailsSource().buildDetails(request)
            );
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }

    }
    filterChain.doFilter(request,response);

} catch (UsernameNotFoundException e) {
    throw new RuntimeException(e);
} catch (IOException e) {
    throw new RuntimeException(e);
} catch (ServletException e) {
    throw new RuntimeException(e);
}
catch (ExpiredJwtException ex) {
    // Handle expired JWT exception
    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    response.getWriter().write("JWT expired"); // Customize the response message as needed
}
    }

}
