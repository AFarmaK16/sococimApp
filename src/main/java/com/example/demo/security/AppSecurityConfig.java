package com.example.demo.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.configuration.EnableGlobalAuthentication;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import com.example.demo.enums.ApplicationUserRole;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.util.concurrent.TimeUnit;

import static com.example.demo.enums.ApplicationUserPermission.*;
import static com.example.demo.enums.ApplicationUserRole.*;
@Configuration
@EnableWebSecurity
//@EnableGlobalMethodSecurity(prePostEnabled = true) ********
public class AppSecurityConfig {
    private final PasswordEncoder passwordEncoder;
    @Autowired
    public AppSecurityConfig(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    //TODO USE JDBC RATHER THAN INMEMORY FOR USER DETAILS
    @Bean
    public UserDetailsService userDetailsService(){
    UserDetails adjaUser = User.builder()
            .username("adja")
            .password(passwordEncoder.encode("passer"))
            //.roles(customer.name())// ROLE_CUSTOMER
            .authorities(customer.getGrantedAuthorities())
            .build();
     UserDetails zaidUser = User.builder()
                .username("zaid")
                .password(passwordEncoder.encode("passer"))
                //.roles(admin.name())// ROLE_ADMIN
                 .authorities(admin.getGrantedAuthorities())

                .build();
     /*UserDetails zaidUser = User.builder()
                .username("zaid")
                .password(passwordEncoder.encode("passer"))
                .roles(admin.name())// ROLE_ADMIN
                .build();*/
          UserDetails deliverUser = User.builder()
                .username("abk")
                .password(passwordEncoder.encode("passer"))
               // .roles(deliver.name())// ROLE_DELIVER
                  .authorities(deliver.getGrantedAuthorities())

                .build();
    return new InMemoryUserDetailsManager(adjaUser,zaidUser,deliverUser);

}

//To secure endpoints
    @Bean
    //look for authorizeRequest deprecated
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        //THE ORDER WE USE FOR REQUESTMATCHERS(ADMIN/CUSTOMER) MATTER
        http
               .csrf().disable() // if enabled logout method should be POST
        /*
        * In the logout we should then have
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout","POST"))
        * */
                .authorizeHttpRequests()
                //NOTE THAT REQUESTMATCHERS CAN BE REPLACED WITH something like @PREAUTHORIZE(HASROLE(ROLE_ADMIN) :: OR /:: hasAuthority('customer:add')

                //BUT FOR THIS WILL NEED TO ADD ****** , by the way it is deprecated, look for equivalent
                .requestMatchers(HttpMethod.GET,"/api/v1/customers/add").hasAuthority(ADD_CUSTOMER.getPermission())
                .requestMatchers(HttpMethod.GET,"/api/v1/products/lists/**").hasAuthority(LIST_PRODUCTS.getPermission())
                .requestMatchers("/api/**").hasRole(admin.name())
                //For an endpoint with more than one roleUser use hasAnyRole
                .anyRequest()
                .authenticated()
                .and()
                .formLogin() //authentication type
               .loginPage("/login").permitAll()
                .defaultSuccessUrl("/products",true)
                .and()
                .rememberMe().tokenValiditySeconds((int) TimeUnit.DAYS.toSeconds(21)).key("somethingverysecured")
                .and()
                .logout()
                .logoutUrl("/logout")
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout","GET"))
                .clearAuthentication(true)
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID","remember-me")
                .logoutSuccessUrl("/login")

                ;
        return http.build();

    }
}
