package com.ssau.kurs_back.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

@Component
public class CookieUtils {

    @Value("${cors.allowed-origins:http://localhost:4200}")
    private String allowedOrigin;

    /**
     * Создаёт httpOnly cookie для токена
     */
    public ResponseCookie createTokenCookie(String name, String token, long maxAgeSeconds) {
        return ResponseCookie.from(name, token)
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(maxAgeSeconds)
                .sameSite("Lax")
                .build();
    }

    /**
     * Создаёт cookie для удаления токена
     */
    public ResponseCookie createDeleteCookie(String name) {
        return ResponseCookie.from(name, "")
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(0)
                .sameSite("Strict")
                .build();
    }

    /**
     * Добавляет cookie в ответ
     */
    public void addCookie(HttpServletResponse response, ResponseCookie cookie) {
        response.addHeader("Set-Cookie", cookie.toString());
    }

    /**
     * Извлекает значение токена из cookie запроса
     */
    public String getTokenFromCookie(jakarta.servlet.http.Cookie[] cookies, String name) {
        if (cookies == null) return null;
        for (Cookie cookie : cookies) {
            if (name.equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        return null;
    }
}