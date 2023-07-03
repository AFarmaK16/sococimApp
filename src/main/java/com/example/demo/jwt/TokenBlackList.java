package com.example.demo.jwt;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component @Setter @Getter
public class TokenBlackList {


    private final Set<String> invalidatedTokens = new HashSet<>();

    public void addInvalidatedToken(String tokenId) {
        invalidatedTokens.add(tokenId);
    }

    public boolean isTokenInvalidated(String username) {
//        System.out.println("this.getInvalidatedTokens()");
//        System.out.println(this.getInvalidatedTokens());
        return invalidatedTokens.contains(username);
    }
}
