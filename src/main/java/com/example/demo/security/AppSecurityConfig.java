package com.example.demo.security;

import com.example.demo.auth.ApplicationUserService;
import com.example.demo.auth.RateLimitAuthenticationFilter;
import com.example.demo.dao.AccountRepository;
import com.example.demo.jwt.JwtAuthenticationFilter;
import com.example.demo.jwt.JwtUsernameAndPasswordAuthenticationFilter;
import jakarta.servlet.Filter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsPasswordService;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
//@EnableGlobalMethodSecurity(prePostEnabled = true) ********
public class AppSecurityConfig {
//    private final PasswordEncoder passwordEncoder;
//    private final ApplicationUserService applicationUserService;
//    private final JwtAuthenticationFilter jwtAuthFilter;
//    private final RateLimitAuthenticationFilter rateLimitAuthenticationFilter;
    private final AuthenticationProvider authenticationProvider;


    //TODO USE JDBC RATHER THAN INMEMORY FOR USER DETAILS



//To secure endpoints
    @Bean
    //look for authorizeRequest deprecated
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        //THE ORDER WE USE FOR REQUESTMATCHERS(ADMIN/CUSTOMER) MATTER
        http
               .csrf().disable() // if enabled logout method should be POST
                .cors().disable()
                .authorizeHttpRequests()
                 .requestMatchers("/api/v1/**","/api/v1/demo/**")// this will be the whitelist
//                 .requestMatchers("/api/v1/auth/**","/api/v1/demo/**")// this will be the whitelist
                .permitAll()
                .anyRequest()//any other request will have to be authenticated
               .authenticated()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)//session isn't gonna be stored in DB | spring will create a new Session for each request
                .and()
                .authenticationProvider(authenticationProvider)
//                .addFilterBefore(rateLimitAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
//                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)


//                .permitAll()

//TODO automatiser le demarrage du serveur au lancement de l'application
                ;
        return http.build();

    }

//    protected void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception{
//        authenticationManagerBuilder.authenticationProvider(daoAuthenticationProvider());
//
//    }


}
