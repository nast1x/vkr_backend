package com.ssau.kurs_back.controller.ref;


import com.ssau.kurs_back.dto.discipline.DisciplineStageCreateDto;
import com.ssau.kurs_back.dto.discipline.DisciplineStageResponseDto;
import com.ssau.kurs_back.dto.discipline.DisciplineStageUpdateDto;
import com.ssau.kurs_back.service.ref.DisciplineStageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ref/discipline-stages")
@RequiredArgsConstructor
public class DisciplineStageController {

    private final DisciplineStageService service;

    @GetMapping
    public ResponseEntity<List<DisciplineStageResponseDto>> getAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DisciplineStageResponseDto> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping
    public ResponseEntity<DisciplineStageResponseDto> create(@Valid @RequestBody DisciplineStageCreateDto dto) {
        DisciplineStageResponseDto created = service.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DisciplineStageResponseDto> update(@PathVariable Integer id,
                                                             @Valid @RequestBody DisciplineStageUpdateDto dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}