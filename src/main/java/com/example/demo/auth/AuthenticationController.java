package com.example.demo.auth;

import ch.qos.logback.core.util.Duration;
import com.example.demo.beans.Account;
import com.example.demo.beans.Customer;
import com.example.demo.controllers.AccountController;
import com.example.demo.controllers.AccountController.AccountRequest;
import com.example.demo.dao.AccountRepository;
import com.example.demo.exception.UsernameNotFoundException;
import com.example.demo.jwt.JwtService;
import com.example.demo.jwt.TokenBlackList;
import com.example.demo.services.AccountService;
import com.twilio.Twilio;
import com.twilio.rest.verify.v2.service.Verification;
import com.twilio.rest.verify.v2.service.VerificationCheck;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;

import javax.security.auth.login.AccountLockedException;
import java.lang.annotation.Annotation;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import static com.twilio.example.ValidationExample.ACCOUNT_SID;
import static com.twilio.example.ValidationExample.AUTH_TOKEN;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthenticationController  {
//public class AuthenticationController implements ExceptionHandler {
    private final AccountService accountService ;
//    @Autowired
//    private final  HttpServletRequest request;
    private final JwtService jwtService;
    private final AccountRepository accountRepository;
    private final TokenBlackList tokenBlacklist;

//    @Autowired
//    private final ClientRegistration registration;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register( //TODO look at null val
            @RequestBody AccountRequest request
    ){
        return ResponseEntity.ok(  accountService.addUser(request,null,"passer"))
      ;
    }
    @PostMapping("/authenticate")
    public ResponseEntity<?> login(
            @RequestBody AuthenticationRequest request
    ) throws AccountLockedException, BadCredentialsException, UsernameNotFoundException , ExecutionException {
            return  accountService.authenticate(request);

    }
    public ResponseEntity<String> generateTOTP() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Customer customer = ((Account) authentication.getPrincipal()).getCustomer();

        Twilio.init(System.getenv("TWILIO_ACCOUNT_SID"), System.getenv("TWILIO_AUTH_TOKEN"));

        Verification verification = Verification.creator(
                        "VAc174056a2bbf3a0b6f0800e69a56623a",
                        customer.getPhoneNumber(),
                        "sms")
                .create();

        System.out.println(verification.getStatus());

        log.info("TOTP has been successfully generated, and awaits your verification {}", LocalDateTime.now());

        return new ResponseEntity<>("Votre code de vérification a bien été envoyé", HttpStatus.OK);
    }

    @PostMapping("/verifyOTP/{otpCode}")
    public ResponseEntity<?> verifyUserTOTP( @PathVariable("otpCode") String otpCode,@RequestBody OTPVerificationRequest otpVerificationRequest
    ) {
        System.out.println("otpCode >>>\t"+ otpCode);
        System.out.println("otpVerificationRequest >>>\t"+ otpVerificationRequest);
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
        System.out.println("The VERIF PART is READY FOR  >>>\t");
        System.out.println(SecurityContextHolder.getContext().getAuthentication());

//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Account account = accountRepository.findAccountByUsername(otpVerificationRequest.username).get();
        Customer customer = account.getCustomer();
//        System.out.println("The authentication is  >>>\t"+ authentication);
//        System.out.println("The account is  >>>\t"+ account);
//        System.out.println("The customer is  >>>\t"+ customer);
        try {

            VerificationCheck verificationCheck = VerificationCheck.creator(
                            "VAc174056a2bbf3a0b6f0800e69a56623a")
                    .setTo("+221"+customer.getPhoneNumber())
                    .setCode(otpVerificationRequest.otpcode)
                    .create();

            System.out.println(verificationCheck.getStatus());

        } catch (Exception e) {
            System.out.println(e.getMessage());

            return new ResponseEntity<>("Verification failed.", HttpStatus.BAD_REQUEST);
        }
               System.out.println("This user's verification has been completed successfully");

        String token = jwtService.generateToken(account);
        ResponseCookie cookie = ResponseCookie.from("jwt", token)
                .secure(true)
                .httpOnly(true)
                .sameSite("Strict")
                .path("/")
                .maxAge(Duration.buildBySeconds(1000*60*40).getMilliseconds())
                .build();
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(new AuthenticationResponse(token,account.getUsername(),account.getRole(),account.getId(), account.isFirst_Logged()));
//        return new ResponseEntity<>("This user's verification has been completed successfully", HttpStatus.OK);

    }
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {
        SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        try {
            System.out.println("EXECUTION OF LOGOUT");

            // Retrieve the token from the request or cookie
            final String authHeader = request.getHeader("Authorization");
            final String jwt;
            final String username;
            logoutHandler.setClearAuthentication(true);
            logoutHandler.setInvalidateHttpSession(true);
           logoutHandler.logout(request, response, authentication);
            // Check if the JWT exists
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                jwt = authHeader.substring(7);

                // Will extract the username from JWT token
                username = jwtService.extractUsername(jwt);

                // Add the token ID to the token blacklist
                tokenBlacklist.addInvalidatedToken(jwt);
                System.out.println("Token blacklisted");

                // Clear the token cookie
                ResponseCookie cookie = ResponseCookie.from("jwt", "")
                        .secure(true)
                        .httpOnly(true)
                        .sameSite("Strict")
                        .path("/")
                        .maxAge(0)
                        .build();
                // Return a response indicating successful logout
                return ResponseEntity.ok()
                        .header(HttpHeaders.SET_COOKIE, cookie.toString())
                        .build();
            } else {
                // Token is missing or has an invalid format
                throw new IllegalArgumentException("Invalid token format");
            }
        } catch (ExpiredJwtException e) {
            // Token has expired, treat it as a successful logout
            System.out.println("Token expired, successful logout");
            return ResponseEntity.ok().build();
        }

    }




//    @Override
//    public Class<? extends Throwable>[] value() {
//        return new Class[0];
//    }
//
//    @Override
//    public Class<? extends Annotation> annotationType() {
//        return null;
//    }
//    @PostMapping("/api/logout")
//    public ResponseEntity<?> logout(HttpServletRequest request,
//                                    @AuthenticationPrincipal(expression = "idToken") OidcIdToken idToken) {
//        // send logout URL to client so they can initiate logout
//        String logoutUrl = this.registration.getProviderDetails()
//                .getConfigurationMetadata().get("end_session_endpoint").toString();
//
//        Map<String, String> logoutDetails = new HashMap<>();
////        Map<String, String> logoutDetails = new HashMap<>();
//        logoutDetails.put("logoutUrl", logoutUrl);
////        logoutDetails.put("idToken", idToken);
//        logoutDetails.put("idToken", idToken.getTokenValue());
//        request.getSession(false).invalidate();
//        return ResponseEntity.ok().body(logoutDetails);
//    }


    public record OTPVerificationRequest(

            String username,
            String otpcode

//             User user,
//             Customer customer

    ) {

    }
}
