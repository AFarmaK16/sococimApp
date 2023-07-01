package com.example.demo.security;

import com.example.demo.auth.AuthenticationFailureListener;
import com.example.demo.auth.AuthenticationSuccessListener;
import com.example.demo.enums.ApplicationUserRole;
import com.example.demo.jwt.JwtAuthenticationFilter;
import com.example.demo.security.filters.CookieCsrfFilter;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

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
                .cors().and()
//                .cors().disable()
                .authorizeHttpRequests()
//     *****************           WHITELISTED
                .requestMatchers("/", "/index.html", "/static/**",
                        "/*.ico", "/*.json", "/*.png", "/api/v1/products/lists/**","/login","/logout","/api/v1/tarifs/list/**","/api/v1/payModes/**"
                ).permitAll()
                .requestMatchers(HttpMethod.POST,"/api/v1/auth/**").permitAll()
                .requestMatchers(HttpMethod.GET,"/api/v1/auth/**").permitAll()
//    ************            WHITELISTED
//FOR OPTIONS
                .requestMatchers(HttpMethod.OPTIONS,"/**").permitAll() //DELETE IT AFTER WAS JUST FOR LOGIN TROUGH //localhost:30000/
//                                ACCOUNTS
                //request for all

                .requestMatchers(HttpMethod.GET,"/api/v1/accounts/**","/api/v1/orderSettings/**").hasAnyRole(String.valueOf(ApplicationUserRole.ADMIN),
                        String.valueOf(ApplicationUserRole.COMMERCIAL),String.valueOf(ApplicationUserRole.CUSTOMER))
                .requestMatchers(HttpMethod.POST,"/api/v1/accounts/account/**").hasRole(String.valueOf(ApplicationUserRole.ADMIN))
                //request for all
//ORDERS
                .requestMatchers(HttpMethod.GET,"/api/v1/customers/**","/api/v1/orders/downloadFile/**").permitAll()
                .requestMatchers(HttpMethod.POST,"/api/v1/orders/add","/api/v1/orders/calculateTotal").hasRole(String.valueOf(ApplicationUserRole.CUSTOMER))
                .requestMatchers(HttpMethod.PUT,"/api/v1/orders/validate/**").hasRole(String.valueOf(ApplicationUserRole.ADV))
                .requestMatchers(HttpMethod.GET,"/api/v1/orders/lists/**").hasAnyRole(String.valueOf(ApplicationUserRole.ADV),String.valueOf(ApplicationUserRole.COMMERCIAL))
// SETTINGS
                .requestMatchers(HttpMethod.POST,"/api/v1/orderSettings/**","/api/v1/products/add","/api/v1/tarifs/new","/api/v1/payModes/new").hasRole(String.valueOf(ApplicationUserRole.ADMIN))
//                .requestMatchers(HttpMethod.GET,"/api/v1/tarifs/new").hasRole(String.valueOf(ApplicationUserRole.ADMIN))

                .requestMatchers(HttpMethod.PUT,"/api/v1/orderSettings/**","/api/v1/products/**","/api/v1/tarifs/delete/**","/api/v1/payModes/delete/**").hasRole(String.valueOf(ApplicationUserRole.ADMIN))

                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)//session isn't gonna be stored in DB | spring will create a new Session for each request
                .and()
                .authenticationProvider(authenticationProvider)
                      .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
//                .addFilterAfter(new CookieCsrfFilter(), BasicAuthenticationFilter.class)





//TODO automatiser le demarrage du serveur au lancement de l'application
                ;
        return http.build();

    }

}
