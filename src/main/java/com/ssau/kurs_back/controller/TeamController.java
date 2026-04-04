package com.ssau.kurs_back.controller;

import com.ssau.kurs_back.dto.team.TeamCreateDto;
import com.ssau.kurs_back.dto.team.TeamResponseDto;
import com.ssau.kurs_back.dto.team.TeamUpdateDto;
import com.ssau.kurs_back.service.TeamService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/teams")
@RequiredArgsConstructor
public class TeamController {

    private final TeamService service;

    @GetMapping
    public ResponseEntity<List<TeamResponseDto>> getAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TeamResponseDto> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @GetMapping("/by-name")
    public ResponseEntity<TeamResponseDto> getByName(@RequestParam String name) {
        return ResponseEntity.ok(service.findByName(name));
    }

    @GetMapping("/search")
    public ResponseEntity<List<TeamResponseDto>> searchByName(@RequestParam String name) {
        return ResponseEntity.ok(service.searchByName(name));
    }

    @GetMapping("/sport-type/{sportTypeId}")
    public ResponseEntity<List<TeamResponseDto>> getBySportTypeId(@PathVariable Integer sportTypeId) {
        return ResponseEntity.ok(service.findBySportTypeId(sportTypeId));
    }

    @PostMapping
    public ResponseEntity<TeamResponseDto> create(@Valid @RequestBody TeamCreateDto dto) {
        TeamResponseDto created = service.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TeamResponseDto> update(@PathVariable Integer id,
                                                  @Valid @RequestBody TeamUpdateDto dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}