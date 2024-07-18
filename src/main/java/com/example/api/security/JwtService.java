package com.example.api.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;

@Service
public class JwtService {
    private final String secretKey = "404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970";
    private final long jwtExpiration = 60000 * 2;

    private Key getSignInKey() {
        var keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private Claims extractAllClaims(String token) {
        var parser = Jwts.parserBuilder()
                .setSigningKey(getSignInKey())
                .build();
        return parser.parseClaimsJws(token).getBody();
    }

    public String extractEmailFromToken(String token) {
        var allClaims = extractAllClaims(token);
        return allClaims.getSubject();
    }

    private boolean isTokenExpired(String token) {
        var allClaims = extractAllClaims(token);
        var expiration = allClaims.getExpiration();
        return expiration.before(new Date());
    }

    public boolean isTokenValid(String token, String username1) {
        final var username2 = extractEmailFromToken(token);
        var isUsernameValid = username1.equals(username2);
        var isTokenExpired = isTokenExpired(token);
        return isUsernameValid && !isTokenExpired;
    }

    public String generateToken(UserDetails userDetails) {
        return Jwts
                .builder()
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }
}