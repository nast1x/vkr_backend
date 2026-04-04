package com.ssau.kurs_back.controller;

import com.ssau.kurs_back.dto.competition.CompetitionCreateDto;
import com.ssau.kurs_back.dto.competition.CompetitionDetailDto;
import com.ssau.kurs_back.dto.competition.CompetitionListDto;
import com.ssau.kurs_back.dto.competition.CompetitionResponseDto;
import com.ssau.kurs_back.dto.competition.CompetitionUpdateDto;
import com.ssau.kurs_back.service.CompetitionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
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

    // ✅ Новый эндпоинт: подробная информация (ВАЖНО: порядок имеет значение!)
    @GetMapping("/list/{id}")
    public ResponseEntity<CompetitionDetailDto> getCompetitionDetail(@PathVariable Integer id) {
        return ResponseEntity.ok(service.findCompetitionDetail(id));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CompetitionResponseDto> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @GetMapping("/sport-type/{sportTypeId}")
    public ResponseEntity<List<CompetitionResponseDto>> getBySportTypeId(@PathVariable Integer sportTypeId) {
        return ResponseEntity.ok(service.findBySportTypeId(sportTypeId));
    }

    @GetMapping("/competition-type/{competitionTypeId}")
    public ResponseEntity<List<CompetitionResponseDto>> getByCompetitionTypeId(@PathVariable Integer competitionTypeId) {
        return ResponseEntity.ok(service.findByCompetitionTypeId(competitionTypeId));
    }

    @GetMapping("/sport-facility/{sportFacilityId}")
    public ResponseEntity<List<CompetitionResponseDto>> getBySportFacilityId(@PathVariable Integer sportFacilityId) {
        return ResponseEntity.ok(service.findBySportFacilityId(sportFacilityId));
    }

    @GetMapping("/date-range")
    public ResponseEntity<List<CompetitionResponseDto>> getByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return ResponseEntity.ok(service.findByDateRange(startDate, endDate));
    }

    @GetMapping("/upcoming")
    public ResponseEntity<List<CompetitionResponseDto>> getUpcoming() {
        return ResponseEntity.ok(service.findUpcoming());
    }

    @GetMapping("/search")
    public ResponseEntity<List<CompetitionResponseDto>> searchByOrganizer(@RequestParam String organizer) {
        return ResponseEntity.ok(service.searchByOrganizer(organizer));
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