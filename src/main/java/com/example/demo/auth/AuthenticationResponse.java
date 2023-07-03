package com.example.demo.auth;

import com.example.demo.enums.ApplicationUserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class AuthenticationResponse {
    private String token;//token that will be sent back to the customer/user
    private String username;
    private ApplicationUserRole role;
    private  int id;
    private  boolean firstLogged;

    public AuthenticationResponse(String username) {
        this.username = username;
    }
}
