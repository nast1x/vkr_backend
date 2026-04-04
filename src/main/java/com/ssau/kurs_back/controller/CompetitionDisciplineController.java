package com.ssau.kurs_back.controller;

import com.ssau.kurs_back.dto.discipline.CompetitionDisciplineCreateDto;
import com.ssau.kurs_back.dto.discipline.CompetitionDisciplineResponseDto;
import com.ssau.kurs_back.dto.discipline.CompetitionDisciplineUpdateDto;
import com.ssau.kurs_back.service.CompetitionDisciplineService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/competition-disciplines")
@RequiredArgsConstructor
public class CompetitionDisciplineController {

    private final CompetitionDisciplineService service;

    @GetMapping
    public ResponseEntity<List<CompetitionDisciplineResponseDto>> getAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CompetitionDisciplineResponseDto> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @GetMapping("/competition/{competitionId}")
    public ResponseEntity<List<CompetitionDisciplineResponseDto>> getByCompetitionId(
            @PathVariable Integer competitionId) {
        return ResponseEntity.ok(service.findByCompetitionId(competitionId));
    }

    @GetMapping("/discipline-type/{disciplineTypeId}")
    public ResponseEntity<List<CompetitionDisciplineResponseDto>> getByDisciplineTypeId(
            @PathVariable Integer disciplineTypeId) {
        return ResponseEntity.ok(service.findByDisciplineTypeId(disciplineTypeId));
    }

    @GetMapping("/stage/{stageId}")
    public ResponseEntity<List<CompetitionDisciplineResponseDto>> getByStageId(
            @PathVariable Integer stageId) {
        return ResponseEntity.ok(service.findByStageId(stageId));
    }

    @PostMapping
    public ResponseEntity<CompetitionDisciplineResponseDto> create(
            @Valid @RequestBody CompetitionDisciplineCreateDto dto) {
        CompetitionDisciplineResponseDto created = service.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CompetitionDisciplineResponseDto> update(
            @PathVariable Integer id,
            @Valid @RequestBody CompetitionDisciplineUpdateDto dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}