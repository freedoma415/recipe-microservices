package com.example.apigateway;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;
import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {
    private static final String SECRET = "my-32-character-ultra-secure-enterprise-secret-key";
    private final Key key = Keys.hmacShaKeyFor(SECRET.getBytes());

    public void validateToken(final String token) {
        Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
    }

    public String generateTestToken() {
        return Jwts.builder()
                .setSubject("admin")
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000L * 60 * 60 * 24 * 365)) // 1 year
                .signWith(key)
                .compact();
    }
}