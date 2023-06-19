package com.example.demo.security;

import com.example.demo.auth.AuthenticationFailureListener;
import com.example.demo.auth.AuthenticationSuccessListener;
import com.example.demo.jwt.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static com.example.demo.enums.ApplicationUserPermission.ADD_CUSTOMER;
import static com.example.demo.enums.ApplicationUserPermission.LIST_PRODUCTS;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
//@EnableGlobalMethodSecurity(prePostEnabled = true) ********
public class AppSecurityConfig {
    private final PasswordEncoder passwordEncoder;
//    private final ApplicationUserService applicationUserService;
    private final JwtAuthenticationFilter jwtAuthFilter;
//    private final RateLimitAuthenticationFilter rateLimitAuthenticationFilter;
    private final AuthenticationProvider authenticationProvider;
//    private final AccountService accountService;
    private final AuthenticationFailureListener authenticationFailureListener; // Injection du bean
    private final AuthenticationSuccessListener authenticationSuccessListener; // Injection du bean

//   @Autowired
//    private  AuthenticationFailureHandler authenticationFailureHandler;
    private final ApplicationConfig applicationConfig;
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
//                 .requestMatchers(HttpMethod.POST,"/api/v1/**","/api/v1/demo/**")// this will be the whitelist
//                .requestMatchers("/api/v1/**","/api/v1/demo/**")// this will be the whitelist
                .requestMatchers(HttpMethod.GET,"/api/v1/customers/add").hasAuthority(ADD_CUSTOMER.getPermission())
                .requestMatchers(HttpMethod.GET,"/api/v1/products/lists/**").hasAuthority(LIST_PRODUCTS.getPermission())
                .requestMatchers(HttpMethod.POST,"/api/v1/auth/**").permitAll()
//                .requestMatchers("/api/v1/**").hasRole(admin.name())
//                 .requestMatchers("/api/v1/auth/**","/api/v1/demo/**")// this will be the whitelist
                .requestMatchers(
                        HttpMethod.GET,
                        "/index/*", "/static/**", "/*.js", "/*.json", "/*.ico")
                .permitAll()
                .anyRequest()//any other request will have to be authenticated
               .authenticated()

                .and()
                .formLogin()
//                .failureHandler(authenticationFailureHandler)
                .loginPage("/index.html")
//                .loginProcessingUrl("/perform_login")
//                .defaultSuccessUrl("/homepage.html",true)
//                .failureUrl("/login?error=true")
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)//session isn't gonna be stored in DB | spring will create a new Session for each request
                .and()
                .authenticationProvider(authenticationProvider)



//                .addFilterBefore(rateLimitAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
//                .addFilterBefore(authenticationFailureListener, UsernamePasswordAuthenticationFilter.class)
//                .addFilterBefore(authenticationSuccessListener, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)


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
