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
import org.springframework.web.multipart.MultipartFile;

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
            @AuthenticationPrincipal UserDetails userDetails) {
        UserResponseDto user = service.findByEmail(userDetails.getUsername());
        return ResponseEntity.ok(service.getUserProfile(user.getIdUser()));
    }

    @PostMapping("/profile/photo")
    public ResponseEntity<UserProfileDto> uploadPhoto(
            @RequestParam("file") MultipartFile file,
            @AuthenticationPrincipal UserDetails userDetails) {
        UserResponseDto user = service.findByEmail(userDetails.getUsername());
        UserProfileDto updatedProfile = service.updateUserPhoto(user.getIdUser(), file);
        return ResponseEntity.ok(updatedProfile);
    }

    @PatchMapping("/{id}/role")
    public ResponseEntity<UserResponseDto> updateRole(
            @PathVariable Integer id,
            @RequestParam Integer roleId) {
        return ResponseEntity.ok(service.updateRole(id, roleId));
    }
}