package com.example.roomy.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration}")
    private long jwtExpiration;

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }

    public String generateToken(String username, Set<String> roles) {
        return Jwts.builder()
                   .setSubject(username)
                   .claim("roles", roles)
                   .setIssuedAt(new Date())
                   .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))
                   .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                   .compact();

    }

    public String extractUsername(String token) {
        return Jwts.parserBuilder()
                   .setSigningKey(getSigningKey())
                   .build()
                   .parseClaimsJws(token)
                   .getBody()
                   .getSubject();
    }

    public List<String> extractRoles(String token) {
        Object roles = Jwts.parserBuilder()
                           .setSigningKey(getSigningKey())
                           .build()
                           .parseClaimsJws(token)
                           .getBody()
                           .get("roles");

        if (roles instanceof List<?>) {
            return ((List<?>) roles).stream()
                                    .filter(role -> role instanceof String)
                                    .map(role -> (String) role)
                                    .toList();
        }

        return new ArrayList<>();
    }
}
