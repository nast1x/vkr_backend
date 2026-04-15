package com.ssau.kurs_back.controller;

import com.ssau.kurs_back.dto.achievement.AchievementCreateDto;
import com.ssau.kurs_back.dto.achievement.AchievementResponseDto;
import com.ssau.kurs_back.dto.achievement.AchievementUpdateDto;
import com.ssau.kurs_back.dto.achievement.SportRankAssignmentDto;
import com.ssau.kurs_back.security.JwtService;
import com.ssau.kurs_back.service.AchievementService;
import com.ssau.kurs_back.util.CookieUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/achievements")
@RequiredArgsConstructor
public class AchievementController {

    private final AchievementService service;
    private final JwtService jwtService;
    private final CookieUtils cookieUtils;

    @GetMapping
    public ResponseEntity<List<AchievementResponseDto>> getAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AchievementResponseDto> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<AchievementResponseDto>> getByUserId(@PathVariable Integer userId) {
        return ResponseEntity.ok(service.findByUserId(userId));
    }

    @GetMapping("/sport-rank/{sportRankId}")
    public ResponseEntity<List<AchievementResponseDto>> getBySportRankId(@PathVariable Integer sportRankId) {
        return ResponseEntity.ok(service.findBySportRankId(sportRankId));
    }

    @GetMapping("/sport-type/{sportTypeId}")
    public ResponseEntity<List<AchievementResponseDto>> getBySportTypeId(@PathVariable Integer sportTypeId) {
        return ResponseEntity.ok(service.findBySportTypeId(sportTypeId));
    }

    @PostMapping
    public ResponseEntity<AchievementResponseDto> create(@Valid @RequestBody AchievementCreateDto dto) {
        AchievementResponseDto created = service.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PostMapping("/assign-rank")
    public ResponseEntity<Void> assignRank(
            @RequestBody SportRankAssignmentDto dto,
            HttpServletRequest request) {

        // Извлекаем ID текущего пользователя (кто нажал кнопку) из куки/токена
        String accessToken = cookieUtils.getTokenFromCookie(request.getCookies(), "access_token");
        if (accessToken == null || accessToken.isBlank()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Integer currentUserId = jwtService.extractUserId(accessToken);

        // Вызываем логику
        service.assignSportRank(dto, currentUserId);

        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<AchievementResponseDto> update(@PathVariable Integer id,
                                                         @Valid @RequestBody AchievementUpdateDto dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}