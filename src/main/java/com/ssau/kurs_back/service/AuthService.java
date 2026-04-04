package com.ssau.kurs_back.service;

import com.ssau.kurs_back.dto.auth.AuthRequest;
import com.ssau.kurs_back.dto.auth.RegisterRequest;
import com.ssau.kurs_back.entity.User;
import com.ssau.kurs_back.entity.ref.Role;
import com.ssau.kurs_back.exception.DuplicateResourceException;
import com.ssau.kurs_back.exception.ResourceNotFoundException;
import com.ssau.kurs_back.repository.UserRepository;
import com.ssau.kurs_back.repository.ref.RoleRepository;
import com.ssau.kurs_back.security.JwtService;
import com.ssau.kurs_back.util.CookieUtils;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final CookieUtils cookieUtils;

    /**
     * Регистрация нового пользователя
     */
    @Transactional
    public void register(RegisterRequest request, HttpServletResponse response) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("Пользователь с таким email уже существует", request.getEmail());
        }

        // 1. Указываем роль по умолчанию (например, "ROLE_USER")
        String defaultRoleName = "User";

        // 2. Ищем роль в базе данных
        Role role = roleRepository.findByNameContainingIgnoreCase(defaultRoleName)
                .stream()
                .filter(r -> r.getName().equalsIgnoreCase(defaultRoleName))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Роль", defaultRoleName));

        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .middleName(request.getMiddleName())
                .role(role)
                .gender(request.getGender())
                .build();

        userRepository.save(user);
        issueTokens(user, response);
    }

    /**
     * Аутентификация пользователя
     */
    @Transactional
    public void login(AuthRequest request, HttpServletResponse response) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("Пользователь", request.getEmail()));

        issueTokens(user, response);
    }

    /**
     * Обновление токенов (ротация refresh токена)
     */
    @Transactional
    public void refreshTokens(String refreshToken, HttpServletResponse response) {
        // ✅ Валидируем refresh токен
        if (!jwtService.validateToken(refreshToken, "refresh")) {
            throw new RuntimeException("Невалидный refresh токен");
        }

        Integer userId = jwtService.extractUserId(refreshToken);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Пользователь", userId));

        // ✅ Генерируем НОВУЮ пару токенов (ротация)
        // Старый refresh токен технически ещё валиден до expiry, но клиент его не использует
        issueTokens(user, response);
    }

    /**
     * Выход из системы (очистка cookies)
     */
    public void logout(HttpServletResponse response) {
        cookieUtils.addCookie(response, cookieUtils.createDeleteCookie("access_token"));
        cookieUtils.addCookie(response, cookieUtils.createDeleteCookie("refresh_token"));
    }

    /**
     * Вспомогательный метод: генерация и установка токенов в cookies
     */
    private void issueTokens(User user, HttpServletResponse response) {
        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        // ✅ Access token: 15 минут
        cookieUtils.addCookie(response,
                cookieUtils.createTokenCookie("access_token", accessToken, 900));  // 900 секунд = 15 минут

        // ✅ Refresh token: 30 дней
        cookieUtils.addCookie(response,
                cookieUtils.createTokenCookie("refresh_token", refreshToken, 2592000));  // 30 дней в секундах
    }
}