package com.example.demo.auth;

import com.example.demo.controllers.AccountController;
import com.example.demo.controllers.AccountController.AccountRequest;
import com.example.demo.services.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AccountService accountService ;
    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register( //TODO look at null val
            @RequestBody AccountRequest request
    ){
        return ResponseEntity.ok(  accountService.addUser(request,null))
      ;
    }
    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody AuthenticationRequest request
    ){
        return ResponseEntity.ok(  accountService.authenticate(request));
    }
}
