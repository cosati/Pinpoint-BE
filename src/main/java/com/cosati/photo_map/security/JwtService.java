package com.cosati.photo_map.security;

import java.security.Key;
import java.time.Duration;
import java.util.Date;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.cosati.photo_map.domain.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

  private final Key signingKey;
  private final long expirationMillis;

  public JwtService(
      @Value("${jwt.secret}") String secret,
      @Value("${jwt.expiration-days:7}") long expirationDays) {
    this.signingKey = Keys.hmacShaKeyFor(secret.getBytes());
    this.expirationMillis = expirationDays * 24 * 60 * 60 * 1000;
  }

  public Duration getExpiration() {
    return Duration.ofMillis(expirationMillis);
  }

  public String generateToken(User user) {
    Date now = new Date();
    Date expiry = new Date(now.getTime() + expirationMillis);

    return Jwts.builder()
        .subject(user.getEmail())
        .claim("userId", user.getId().toString())
        .issuedAt(now)
        .expiration(expiry)
        .signWith(signingKey)
        .compact();
  }

  public boolean isValid(String token) {
    try {
      parseClaims(token);
      return true;
    } catch (JwtException | IllegalArgumentException e) {
      return false;
    }
  }

  public String extractEmail(String token) {
    return parseClaims(token).getSubject();
  }

  public UUID extractUserId(String token) {
    return UUID.fromString(parseClaims(token).get("userId", String.class));
  }

  private Claims parseClaims(String token) {
    return Jwts.parser()
        .verifyWith((javax.crypto.SecretKey) signingKey)
        .build()
        .parseSignedClaims(token)
        .getPayload();
  }
}
