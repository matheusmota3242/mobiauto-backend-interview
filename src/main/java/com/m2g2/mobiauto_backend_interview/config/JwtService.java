package com.m2g2.mobiauto_backend_interview.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.function.Function;

@Service
public class JwtService {

    @Value("${secret}")
    private String secretKey;
    public String extractUsername(String token) {
        return extrairClaim(token, Claims::getSubject);
    }

    public String generateToken(UserDetails userDetails) {
        final long emissionMillis = System.currentTimeMillis();
        return Jwts.builder()
                .subject(userDetails.getUsername())
                .issuedAt(new Date(emissionMillis))
                .expiration(new Date(emissionMillis + 1000 * 60 * 60 * 24))
                .signWith(getSignInKey())
                .compact();
    }

    public boolean isTokenValido(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpirado(token));
    }

    public <T> T extrairClaim(String token, Function<Claims, T> claimsResolver) {
        if (token.startsWith(JwtFilter.BEARER_PREFIX)) {
            token = token.substring(JwtFilter.BEARER_PREFIX.length());
        }
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private boolean isTokenExpirado(String token) {
        return extrairClaim(token, Claims::getExpiration).before(new Date());
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSignInKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private SecretKey getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
