package com.ssau.kurs_back.security;

import com.ssau.kurs_back.entity.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.UUID;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class JwtService {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.access-token-expiration:900000}")
    private long accessTokenExpiration;

    @Value("${jwt.refresh-token-expiration:2592000000}")
    private long refreshTokenExpiration;

    @Value("${jwt.issuer:sport-kurs-api}")
    private String issuer;

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Генерация access токена
     */
    public String generateAccessToken(User user) {
        return Jwts.builder()
                .subject(String.valueOf(user.getIdUser()))
                .claim("email", user.getEmail())
                .claim("role", user.getRole().getName())
                .claim("type", "access")
                .issuer(issuer)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + accessTokenExpiration))
                .signWith(getSigningKey())
                .compact();
    }

    /**
     * Генерация refresh токена с уникальным tokenId для ротации
     */
    public String generateRefreshToken(User user) {
        return Jwts.builder()
                .subject(String.valueOf(user.getIdUser()))
                .claim("email", user.getEmail())
                .claim("type", "refresh")
                .claim("tokenId", UUID.randomUUID().toString())  // ✅ Уникальный ID для ротации
                .issuer(issuer)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + refreshTokenExpiration))
                .signWith(getSigningKey())
                .compact();
    }

    /**
     * Извлечение claim из токена
     */
    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    /**
     * Валидация токена (подпись + срок + тип)
     */
    public boolean validateToken(String token, String tokenType) {
        try {
            Claims claims = extractAllClaims(token);
            return tokenType.equals(claims.get("type", String.class))
                    && claims.getExpiration().after(new Date());
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    /**
     * Получение userId из токена
     */
    public Integer extractUserId(String token) {
        return Integer.parseInt(extractClaim(token, Claims::getSubject));
    }

    /**
     * Получение email из токена
     */
    public String extractEmail(String token) {
        return extractClaim(token, claims -> claims.get("email", String.class));
    }

    /**
     * Проверка, является ли токен refresh-токеном
     */
    public boolean isRefreshToken(String token) {
        try {
            return "refresh".equals(extractClaim(token, claims -> claims.get("type", String.class)));
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Получение tokenId из refresh токена (для ротации)
     */
    public String extractTokenId(String token) {
        return extractClaim(token, claims -> claims.get("tokenId", String.class));
    }
}