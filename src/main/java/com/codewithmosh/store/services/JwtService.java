package com.codewithmosh.store.services;

import com.codewithmosh.store.entities.Role;
import com.codewithmosh.store.entities.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class JwtService {

    @Value("${spring.jwt.secret}")
    private String secret;

    @Value("${spring.jwt.accessTokenExpiration}")
    private String tokenExpiration;

//    public String generateToken(User email) {
public String generateToken(User user) {
        return Jwts.builder()
                .subject(user.getId().toString())
                .claim("email", user.getEmail())
                .claim("name", user.getName())
                .claim("role", user.getRole())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + Long.valueOf(tokenExpiration)  * 1000))
                .signWith(Keys.hmacShaKeyFor(secret.getBytes()))
                .compact();

    }

    public boolean validateToken(String token) {
        try{
            var claims = getClaims(token);
            return claims.getExpiration().after(new Date());
        } catch (JwtException ex) {
            return false;
        }
    }

    private Claims getClaims(String token) {
        var claims = Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(secret.getBytes()))
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return claims;
    }

    public Long getUserIdFromToken(String token){
        return Long.valueOf(getClaims(token).getSubject());
    };

    public Role getRoleFromToken(String token){
        return Role.valueOf(getClaims(token).get("role", String.class));
    };
//    public String getEmailFromToken(String token){
//        return getClaims(token).getSubject();
//    }
}
