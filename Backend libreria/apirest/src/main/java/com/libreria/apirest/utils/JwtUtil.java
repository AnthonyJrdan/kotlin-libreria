package com.libreria.apirest.utils;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.libreria.apirest.models.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {
    
    private final SecretKey key = Keys.hmacShaKeyFor(
        "b88ee43c17c993758d8440c7e7b68e95f0ef24dff5cd20327132c7881998b273".getBytes(StandardCharsets.UTF_8)
    );

    private Claims getClaims(String token){
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();

    }

    public String extractUsername(String token){
        return getClaims(token).getSubject();
    }

    // Valdiar si el token expira
    private boolean isTokenExpired(String token)
    {
        return getClaims(token).getExpiration().before(new Date());

    }

    // Validar token
    public boolean isTokenValid(String token, UserDetails userDetails){
        String username= extractUsername(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);


    }

    public String generateToken(User user) {
        long expirationMillis= 1000*60*60 * 24; // 24 HORA
        Date now=new Date();
        Date expiry= new Date(now.getTime() + expirationMillis);
        return Jwts.builder()
                .subject(user.getEmail()) // valor a authenticar , email
                .issuedAt(now) // fecha actual
                .expiration(expiry)
                .signWith(key) // llave
                .compact(); 
    }
}
