package com.vlucenco.springframework.storeapp.security;

import com.vlucenco.springframework.storeapp.exception.JwtAuthenticationException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.function.Function;

@Component
public class JwtUtil {
    @Value("${store.app.jwtSecret}")
    private String jwtSecret;

    @Value("${store.app.jwtExpirationMs}")
    private int jwtExpirationMs;

    public String generateToken(String email) {
        return Jwts.builder()
                .subject(email)
                .issuedAt(new Date())
                .expiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(getSigningKey())
                .compact();
    }

    public String getUserNameFromJwtToken(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public boolean validateToken(String token, String userEmail) {
        return userEmail.equals(getUserNameFromJwtToken(token)) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        try {
            Claims claims = parseToken(token);
            return claimsResolver.apply(claims);
        } catch (ExpiredJwtException ex) {
            throw new JwtAuthenticationException("Token has expired. Please log in again.");
        } catch (MalformedJwtException | UnsupportedJwtException ex) {
            throw new JwtAuthenticationException("Invalid token.");
        } catch (IllegalArgumentException ex) {
            throw new JwtAuthenticationException("Token is missing.");
        }
    }

    private Claims parseToken(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
