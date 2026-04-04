package com.ssau.kurs_back.controller;

import com.ssau.kurs_back.dto.result.ResultPersonalCreateDto;
import com.ssau.kurs_back.dto.result.ResultPersonalResponseDto;
import com.ssau.kurs_back.dto.result.ResultPersonalUpdateDto;
import com.ssau.kurs_back.service.ResultPersonalService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/results/personal")
@RequiredArgsConstructor
public class ResultPersonalController {

    private final ResultPersonalService service;

    @GetMapping
    public ResponseEntity<List<ResultPersonalResponseDto>> getAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResultPersonalResponseDto> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ResultPersonalResponseDto>> getByUserId(@PathVariable Integer userId) {
        return ResponseEntity.ok(service.findByUserId(userId));
    }

    @GetMapping("/discipline/{disciplineId}")
    public ResponseEntity<List<ResultPersonalResponseDto>> getByDisciplineId(@PathVariable Integer disciplineId) {
        return ResponseEntity.ok(service.findByDisciplineId(disciplineId));
    }

    @PostMapping
    public ResponseEntity<ResultPersonalResponseDto> create(@Valid @RequestBody ResultPersonalCreateDto dto) {
        ResultPersonalResponseDto created = service.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResultPersonalResponseDto> update(@PathVariable Integer id,
                                                            @Valid @RequestBody ResultPersonalUpdateDto dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}