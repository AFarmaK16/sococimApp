package com.example.demo.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {
    //TODO Navigate to Encryption Generator and choose 256 bit cause
    //it is the size needed for JWT key signature (gonna be the SECRET_KEY value)
    private static final String SECRET_KEY ="635266556A586E327234753778214125442A472D4B6150645367566B59703373";
public boolean isTokenValid(String token, UserDetails userDetails){
    final String username = extractUsername(token);
    return (username.equals(userDetails.getUsername())&& !isTokenExpired(token));
}

    private boolean isTokenExpired(String token) {
    return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
    return extractClaim(token,Claims::getExpiration);
    }

    public String extractUsername(String token) {

        return extractClaim(token,Claims::getSubject);
    }
    private Claims extractAllClaims(String token){
        return Jwts
                .parser()
                .setSigningKey(getSignInKey())
                .parseClaimsJws(token)
                .getBody();
//                .parser() //parserBuilder
//                .setSigningKey(getSignInKey())
//                //.build()
//                .parseClaimsJws(token)
//                .getBody();
    }
    public  <T> T extractClaim(String token, Function<Claims, T> claimsResolver){
        final  Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Key getSignInKey() {
        byte [] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
                return Keys.hmacShaKeyFor(keyBytes);

    }
    //GENERATING TOKEN
    //without claims
    public String generateToken(
            UserDetails userDetails
    ){
        return
               generateToken(new HashMap<>(),userDetails);
    }
//With claims
    private String generateToken(
            Map<String,Object> extractClaims,//to pass authorities ,....
            UserDetails userDetails
    ){
        return
        Jwts.builder()
                .setClaims(extractClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))// set TOKEN creation date
                .setExpiration(new Date(System.currentTimeMillis()+ 1000 * 60 * 24))// set TOKEN expiration date
                .signWith(SignatureAlgorithm.HS256, getSignInKey())
                .compact();
    }


}
