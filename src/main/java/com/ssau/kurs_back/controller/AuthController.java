package com.ssau.kurs_back.controller;

import com.ssau.kurs_back.dto.auth.AuthRequest;
import com.ssau.kurs_back.dto.auth.AuthResponse;
import com.ssau.kurs_back.dto.auth.RegisterRequest;
import com.ssau.kurs_back.service.AuthService;
import com.ssau.kurs_back.util.CookieUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final CookieUtils cookieUtils;

    /**
     * Регистрация нового пользователя
     */
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(
            @Valid @RequestBody RegisterRequest request,
            HttpServletResponse response) {

        authService.register(request, response);

        return ResponseEntity.ok(AuthResponse.builder()
                .message("Регистрация успешна")
                .email(request.getEmail())
                .build());
    }

    /**
     * Аутентификация пользователя
     */
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @Valid @RequestBody AuthRequest request,
            HttpServletResponse response) {

        authService.login(request, response);

        return ResponseEntity.ok(AuthResponse.builder()
                .message("Вход успешен")
                .email(request.getEmail())
                .build());
    }

    /**
     * Обновление токенов (ротация refresh)
     */
    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refresh(
            HttpServletRequest request,
            HttpServletResponse response) {

        String refreshToken = cookieUtils.getTokenFromCookie(request.getCookies(), "refresh_token");

        if (refreshToken == null) {
            return ResponseEntity.badRequest().body(AuthResponse.builder()
                    .message("Refresh токен не найден")
                    .build());
        }

        authService.refreshTokens(refreshToken, response);

        return ResponseEntity.ok(AuthResponse.builder()
                .message("Токены обновлены")
                .build());
    }

    /**
     * Выход из системы
     */
    @PostMapping("/logout")
    public ResponseEntity<AuthResponse> logout(HttpServletResponse response) {
        authService.logout(response);

        return ResponseEntity.ok(AuthResponse.builder()
                .message("Выход успешен")
                .build());
    }

    /**
     * Проверка состояния аутентификации (для отладки)
     */
    @GetMapping("/me")
    public ResponseEntity<AuthResponse> getCurrentUser(HttpServletRequest request) {
        String accessToken = cookieUtils.getTokenFromCookie(request.getCookies(), "access_token");

        if (accessToken == null) {
            return ResponseEntity.status(401).build();
        }

        // Токен уже валидирован фильтром, можно извлечь данные
        return ResponseEntity.ok(AuthResponse.builder()
                .message("Аутентифицирован")
                .build());
    }
}