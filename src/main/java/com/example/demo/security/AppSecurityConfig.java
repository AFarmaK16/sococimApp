package com.example.demo.security;

import com.example.demo.auth.ApplicationUserService;
import com.example.demo.jwt.JwtUsernameAndPasswordAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsPasswordService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
//@EnableGlobalMethodSecurity(prePostEnabled = true) ********
public class AppSecurityConfig {
    private final PasswordEncoder passwordEncoder;
    private final ApplicationUserService applicationUserService;
    @Autowired
    public AppSecurityConfig(PasswordEncoder passwordEncoder, ApplicationUserService applicationUserService) {
        this.passwordEncoder = passwordEncoder;
        this.applicationUserService = applicationUserService;
    }

    //TODO USE JDBC RATHER THAN INMEMORY FOR USER DETAILS



//To secure endpoints
    @Bean
    //look for authorizeRequest deprecated
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        //THE ORDER WE USE FOR REQUESTMATCHERS(ADMIN/CUSTOMER) MATTER
        http
               .csrf().disable() // if enabled logout method should be POST
                .cors().disable()
                .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)//session isn't gonna be stored in DB
                .and()
//                .addFilter(new JwtUsernameAndPasswordAuthenticationFilter(authenticationManager(http)))

                .authorizeHttpRequests()
                .anyRequest()
//               .authenticated()
                .permitAll()

//TODO automatiser le demarrage du serveur au lancement de l'application
                ;
        return http.build();

    }

//    protected void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception{
//        authenticationManagerBuilder.authenticationProvider(daoAuthenticationProvider());
//
//    }
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity httpSecurity)throws  Exception{
        AuthenticationManagerBuilder authenticationManagerBuilder = httpSecurity.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.authenticationProvider(daoAuthenticationProvider());
        return authenticationManagerBuilder.build();
    }
    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider(){
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder);//allow password to be decoded
//        provider.setPasswordEncoder(passwordEncoder);//allow password to be decoded
        provider.setUserDetailsService(applicationUserService);
        System.out.println("Provider 😩 "+provider);
        return provider;
    }
}
