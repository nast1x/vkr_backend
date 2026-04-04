package com.ssau.kurs_back.controller.ref;


import com.ssau.kurs_back.dto.discipline.DisciplineTypeCreateDto;
import com.ssau.kurs_back.dto.discipline.DisciplineTypeResponseDto;
import com.ssau.kurs_back.dto.discipline.DisciplineTypeUpdateDto;
import com.ssau.kurs_back.service.ref.DisciplineTypeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ref/discipline-types")
@RequiredArgsConstructor
public class DisciplineTypeController {

    private final DisciplineTypeService service;

    @GetMapping
    public ResponseEntity<List<DisciplineTypeResponseDto>> getAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DisciplineTypeResponseDto> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @GetMapping("/sport-type/{sportTypeId}")
    public ResponseEntity<List<DisciplineTypeResponseDto>> getBySportTypeId(@PathVariable Integer sportTypeId) {
        return ResponseEntity.ok(service.findBySportTypeId(sportTypeId));
    }

    @GetMapping("/gender/{gender}")
    public ResponseEntity<List<DisciplineTypeResponseDto>> getByGender(@PathVariable String gender) {
        return ResponseEntity.ok(service.findByGender(gender));
    }

    @GetMapping("/participation-type/{type}")
    public ResponseEntity<List<DisciplineTypeResponseDto>> getByParticipationType(@PathVariable String type) {
        return ResponseEntity.ok(service.findByParticipationType(type));
    }

    @PostMapping
    public ResponseEntity<DisciplineTypeResponseDto> create(@Valid @RequestBody DisciplineTypeCreateDto dto) {
        DisciplineTypeResponseDto created = service.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DisciplineTypeResponseDto> update(@PathVariable Integer id,
                                                            @Valid @RequestBody DisciplineTypeUpdateDto dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}