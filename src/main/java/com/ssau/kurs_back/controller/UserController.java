package com.ssau.kurs_back.controller;

import com.ssau.kurs_back.dto.user.*;
import com.ssau.kurs_back.security.JwtService;
import com.ssau.kurs_back.service.UserService;
import com.ssau.kurs_back.util.CookieUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService service;
    private final JwtService jwtService;
    private final CookieUtils cookieUtils;

    @GetMapping
    public ResponseEntity<List<UserResponseDto>> getAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<UserResponseDto> getByEmail(@PathVariable String email) {
        return ResponseEntity.ok(service.findByEmail(email));
    }

    @GetMapping("/search")
    public ResponseEntity<List<UserResponseDto>> searchByLastName(@RequestParam String lastName) {
        return ResponseEntity.ok(service.searchByLastName(lastName));
    }

    @GetMapping("/coach/{coachId}/trainees")
    public ResponseEntity<List<UserResponseDto>> getTraineesByCoachId(@PathVariable Integer coachId) {
        return ResponseEntity.ok(service.findTraineesByCoachId(coachId));
    }

    @GetMapping("/team")
    public ResponseEntity<List<TeamMemberDto>> getTeamMembers() {
        return ResponseEntity.ok(service.findTeamMembers());
    }

    @PostMapping
    public ResponseEntity<UserResponseDto> create(@Valid @RequestBody UserCreateDto dto) {
        UserResponseDto created = service.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDto> update(@PathVariable Integer id,
                                                  @Valid @RequestBody UserUpdateDto dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/profile/{id}")
    public ResponseEntity<UserProfileDto> getUserProfile(@PathVariable Integer id) {
        return ResponseEntity.ok(service.getUserProfile(id));
    }

    @GetMapping("/profile/me")
    public ResponseEntity<UserProfileDto> getCurrentUserProfile(
            @AuthenticationPrincipal UserDetails userDetails,
            HttpServletRequest request) {
        String accessToken = cookieUtils.getTokenFromCookie(request.getCookies(), "access_token");
        if (accessToken == null || accessToken.isBlank()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        Integer userId = jwtService.extractUserId(accessToken);
        return ResponseEntity.ok(service.getUserProfile(userId));
    }

    @PostMapping("/profile/photo")
    public ResponseEntity<UserProfileDto> uploadPhoto(
            @RequestParam("file") org.springframework.web.multipart.MultipartFile file,
            @AuthenticationPrincipal UserDetails userDetails,
            HttpServletRequest request) {

        // Извлекаем ID текущего пользователя из токена (как в вашем методе getCurrentUserProfile)
        String accessToken = cookieUtils.getTokenFromCookie(request.getCookies(), "access_token");
        if (accessToken == null || accessToken.isBlank()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        Integer userId = jwtService.extractUserId(accessToken);

        // Вызываем сервис для сохранения файла и обновления БД
        UserProfileDto updatedProfile = service.updateUserPhoto(userId, file);

        return ResponseEntity.ok(updatedProfile);
    }
}