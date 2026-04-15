package com.ssau.kurs_back.controller;

import com.ssau.kurs_back.dto.competition.*;
import com.ssau.kurs_back.service.CompetitionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/competitions")
@RequiredArgsConstructor
public class CompetitionController {

    private final CompetitionService service;

    @GetMapping
    public ResponseEntity<List<CompetitionResponseDto>> getAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/list")
    public ResponseEntity<List<CompetitionListDto>> getCompetitionList() {
        return ResponseEntity.ok(service.findCompetitionList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CompetitionDetailDto> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(service.findCompetitionDetail(id));
    }

    @PostMapping
    public ResponseEntity<CompetitionResponseDto> create(@Valid @RequestBody CompetitionCreateDto dto) {
        CompetitionResponseDto created = service.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CompetitionResponseDto> update(@PathVariable Integer id,
                                                         @Valid @RequestBody CompetitionUpdateDto dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}