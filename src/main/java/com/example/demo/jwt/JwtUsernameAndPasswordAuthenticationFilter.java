package com.example.demo.jwt;

import com.example.demo.beans.Account;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Date;

@AllArgsConstructor
public class JwtUsernameAndPasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter { //this class is for verifying if JWT exist
    private final AuthenticationManager authenticationManager;
    private static final String SECRET_KEY ="635266556A586E327234753778214125442A472D4B6150645367566B59703373";

//    public JwtUsernameAndPasswordAuthenticationFilter(AuthenticationManager authenticationManager) {
//        this.authenticationManager = authenticationManager;
//    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
       Account account =    new ObjectMapper().readValue(request.getInputStream(), Account.class);
            Authentication authentication = new UsernamePasswordAuthenticationToken(
                   account.getUsername(),
                    account.getPassword()
            );
            Authentication authenticate = authenticationManager.authenticate(authentication);
            return authenticate;//check if user exist and if password is correct , if so authenticated
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

   //following method will be invoked after the previous one is successful
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
       //This method will create a token after successful authentication
        String token = Jwts.builder()
                .setSubject(authResult.getName())//name of the current user
                .claim("authorities", authResult.getAuthorities())
                .setIssuedAt(new Date())
                .setExpiration(java.sql.Date.valueOf(LocalDate.now().plusWeeks(1)))//token will expire within 2 weeks
//                Keys.hmacShaKeyFor("securesecuresecuresecuresecuresecuresecure") //must me as long as possible
                .signWith(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()))
//                .signWith(SignatureAlgorithm.HS256,"secyresecyresecyresecyresecyresecyre")
                .compact();
        //addingtoken to the response
        response.addHeader("Authorization","Bearer "+token);
        super.successfulAuthentication(request, response, chain, authResult);
    }

}
