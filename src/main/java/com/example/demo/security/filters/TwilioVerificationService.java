package com.example.demo.security.filters;

import com.example.demo.auth.AuthenticationResponse;
import com.example.demo.beans.Account;
import com.example.demo.beans.Customer;
import com.twilio.Twilio;
import com.twilio.rest.verify.v2.service.Verification;
import com.twilio.rest.verify.v2.service.VerificationCheck;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import static com.twilio.example.ValidationExample.ACCOUNT_SID;
import static com.twilio.example.ValidationExample.AUTH_TOKEN;

@Slf4j @Service
public class TwilioVerificationService {
    public ResponseEntity<AuthenticationResponse> generateTOTP(Account account) {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        Customer customer = ((Account) authentication.getPrincipal()).getCustomer();

        Twilio.init(System.getenv("TWILIO_ACCOUNT_SID"), System.getenv("TWILIO_AUTH_TOKEN"));

        Verification verification = Verification.creator(
                        "VAc174056a2bbf3a0b6f0800e69a56623a",
                       "+221"+account.getCustomer().getPhoneNumber(),
                        "sms")
                .create();

        System.out.println(verification.getStatus());

        log.info("TOTP has been successfully generated, and awaits your verification {}", LocalDateTime.now());

//        return new ResponseEntity<>("Votre code de vérification a bien été envoyé", HttpStatus.OK);

        return ResponseEntity.ok()
                .body(new AuthenticationResponse(account.getUsername()));
    }
//    public ResponseEntity<?> verifyUserTOTP() {
//        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
//
//        try {
//
//            VerificationCheck verificationCheck = VerificationCheck.creator(
//                            "VAc174056a2bbf3a0b6f0800e69a56623a")
//                    .setTo("+221772010355")
//                    .setCode("934241")
//                    .create();
//
//            System.out.println(verificationCheck.getStatus());
//
//        } catch (Exception e) {
//            return new ResponseEntity<>("Verification failed.", HttpStatus.BAD_REQUEST);
//        }
//
//        return new ResponseEntity<>("This user's verification has been completed successfully", HttpStatus.OK);
//
//    }
}
