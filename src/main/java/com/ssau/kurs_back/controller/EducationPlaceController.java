package com.ssau.kurs_back.controller;

import com.ssau.kurs_back.dto.education.EducationPlaceCreateDto;
import com.ssau.kurs_back.dto.education.EducationPlaceResponseDto;
import com.ssau.kurs_back.dto.education.EducationPlaceUpdateDto;
import com.ssau.kurs_back.service.EducationPlaceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/education-places")
@RequiredArgsConstructor
public class EducationPlaceController {

    private final EducationPlaceService service;

    @GetMapping
    public ResponseEntity<List<EducationPlaceResponseDto>> getAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EducationPlaceResponseDto> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<EducationPlaceResponseDto>> getByUserId(@PathVariable Integer userId) {
        return ResponseEntity.ok(service.findByUserId(userId));
    }

    @GetMapping("/university/{universityId}")
    public ResponseEntity<List<EducationPlaceResponseDto>> getByUniversityId(@PathVariable Integer universityId) {
        return ResponseEntity.ok(service.findByUniversityId(universityId));
    }

    @GetMapping("/major/{majorId}")
    public ResponseEntity<List<EducationPlaceResponseDto>> getByMajorId(@PathVariable Integer majorId) {
        return ResponseEntity.ok(service.findByMajorId(majorId));
    }

    @PostMapping
    public ResponseEntity<EducationPlaceResponseDto> create(@Valid @RequestBody EducationPlaceCreateDto dto) {
        EducationPlaceResponseDto created = service.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EducationPlaceResponseDto> update(@PathVariable Integer id,
                                                            @Valid @RequestBody EducationPlaceUpdateDto dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}