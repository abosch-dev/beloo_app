package com.beloo.test.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

import static java.nio.charset.StandardCharsets.UTF_8;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private int expiration;

    public String generateToken(Authentication authentication) {
        var userDetails = (UserDetails) authentication.getPrincipal();
        return Jwts.builder().subject(userDetails.getUsername())
                .issuedAt(new Date())
                .expiration(new Date(new Date().getTime() + expiration * 1000L))
                .signWith(buildKey())
                .compact();
    }

    public Boolean validateToken(String token, UserDetails userDetails){
        final String userName = extractUserName(token);
        return (userName.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    public Boolean isTokenExpired(String token){
        return extractExpiration(token).before(new Date());
    }

    public Date extractExpiration(String token){
        return extractAllClaims(token).getExpiration();
    }
    public Claims extractAllClaims(String token){
        return Jwts.parser()
                .verifyWith(buildKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private SecretKey buildKey() {
        return Keys.hmacShaKeyFor(secret.getBytes(UTF_8));
    }

    public String extractUserName(String token){
        return extractAllClaims(token).getSubject();
    }
}
