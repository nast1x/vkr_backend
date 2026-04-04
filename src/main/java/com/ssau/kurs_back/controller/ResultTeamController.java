package com.ssau.kurs_back.controller;

import com.ssau.kurs_back.dto.result.ResultTeamCreateDto;
import com.ssau.kurs_back.dto.result.ResultTeamResponseDto;
import com.ssau.kurs_back.dto.result.ResultTeamUpdateDto;
import com.ssau.kurs_back.service.ResultTeamService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/results/team")
@RequiredArgsConstructor
public class ResultTeamController {

    private final ResultTeamService service;

    @GetMapping
    public ResponseEntity<List<ResultTeamResponseDto>> getAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResultTeamResponseDto> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @GetMapping("/team/{teamId}")
    public ResponseEntity<List<ResultTeamResponseDto>> getByTeamId(@PathVariable Integer teamId) {
        return ResponseEntity.ok(service.findByTeamId(teamId));
    }

    @GetMapping("/discipline/{disciplineId}")
    public ResponseEntity<List<ResultTeamResponseDto>> getByDisciplineId(@PathVariable Integer disciplineId) {
        return ResponseEntity.ok(service.findByDisciplineId(disciplineId));
    }

    @PostMapping
    public ResponseEntity<ResultTeamResponseDto> create(@Valid @RequestBody ResultTeamCreateDto dto) {
        ResultTeamResponseDto created = service.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResultTeamResponseDto> update(@PathVariable Integer id,
                                                        @Valid @RequestBody ResultTeamUpdateDto dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}