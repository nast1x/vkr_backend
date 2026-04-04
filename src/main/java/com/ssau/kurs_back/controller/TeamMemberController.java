package com.ssau.kurs_back.controller;

import com.ssau.kurs_back.dto.team.TeamMemberCreateDto;
import com.ssau.kurs_back.dto.team.TeamMemberResponseDto;
import com.ssau.kurs_back.dto.team.TeamMemberUpdateDto;
import com.ssau.kurs_back.service.TeamMemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/team-members")
@RequiredArgsConstructor
public class TeamMemberController {

    private final TeamMemberService service;

    @GetMapping
    public ResponseEntity<List<TeamMemberResponseDto>> getAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{teamId}/{userId}")
    public ResponseEntity<TeamMemberResponseDto> getById(
            @PathVariable Integer teamId,
            @PathVariable Integer userId) {
        return ResponseEntity.ok(service.findById(teamId, userId));
    }

    @GetMapping("/team/{teamId}")
    public ResponseEntity<List<TeamMemberResponseDto>> getByTeamId(@PathVariable Integer teamId) {
        return ResponseEntity.ok(service.findByTeamId(teamId));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<TeamMemberResponseDto>> getByUserId(@PathVariable Integer userId) {
        return ResponseEntity.ok(service.findByUserId(userId));
    }

    @PostMapping
    public ResponseEntity<TeamMemberResponseDto> create(@Valid @RequestBody TeamMemberCreateDto dto) {
        TeamMemberResponseDto created = service.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{teamId}/{userId}")
    public ResponseEntity<TeamMemberResponseDto> update(
            @PathVariable Integer teamId,
            @PathVariable Integer userId,
            @Valid @RequestBody TeamMemberUpdateDto dto) {
        return ResponseEntity.ok(service.update(teamId, userId, dto));
    }

    @DeleteMapping("/{teamId}/{userId}")
    public ResponseEntity<Void> delete(
            @PathVariable Integer teamId,
            @PathVariable Integer userId) {
        service.deleteById(teamId, userId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/team/{teamId}")
    public ResponseEntity<Void> deleteByTeamId(@PathVariable Integer teamId) {
        service.deleteByTeamId(teamId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/user/{userId}")
    public ResponseEntity<Void> deleteByUserId(@PathVariable Integer userId) {
        service.deleteByUserId(userId);
        return ResponseEntity.noContent().build();
    }
}