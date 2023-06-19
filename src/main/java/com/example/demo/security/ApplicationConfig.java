package com.example.demo.security;

import com.example.demo.auth.ApplicationUserService;
import com.example.demo.auth.LoginAttemptService;
import com.example.demo.dao.AccountRepository;
import com.example.demo.services.EmailService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration  @RequiredArgsConstructor
public class ApplicationConfig {
    private final AccountRepository accountRepository;
//    private final ApplicationUserService applicationUserService;
    private final PasswordEncoder passwordEncoder;
    private final LoginAttemptService loginAttemptService;
    private final EmailService emailService;
    @Bean
    public UserDetailsService userDetailsService(){
        return new ApplicationUserService(accountRepository,loginAttemptService,emailService);
//        return username -> accountRepository.findAccountByUsername(username)
//                            .orElseThrow(()->new UsernameNotFoundException(
//                                    String.format("Username not found ",username)
//                            ));
       }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity httpSecurity)throws  Exception{
        AuthenticationManagerBuilder authenticationManagerBuilder = httpSecurity.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.authenticationProvider(daoAuthenticationProvider());
        return authenticationManagerBuilder.build();
    }
    //TODO CHANGE FETCHTYPE TO LAZY LOOK FOR BOUALI SECURITY GIT
    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider(){
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder);//allow password to be decoded
//        provider.setPasswordEncoder(passwordEncoder);//allow password to be decoded
        provider.setUserDetailsService(userDetailsService());
//        System.out.println("Provider 😩 "+provider);
        return provider;
    }


//    @Bean
//    public PasswordEncoder passwordEncoder(){
//            return new BCryptPasswordEncoder();
//    }
}
