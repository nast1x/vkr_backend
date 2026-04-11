package com.ssau.kurs_back.controller.ref;

import com.ssau.kurs_back.dto.university.UniversityDetailDto;
import com.ssau.kurs_back.dto.university.UniversityStatsDto;
import com.ssau.kurs_back.entity.ref.University;
import com.ssau.kurs_back.service.ref.UniversityService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/ref/university")
@RequiredArgsConstructor
public class UniversityController {

    private final UniversityService service;

    @GetMapping
    public List<University> getAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<University> getById(@PathVariable Integer id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/search")
    public List<University> search(@RequestParam String name) {
        return service.searchByName(name);
    }

    @GetMapping("/stats")
    public ResponseEntity<List<UniversityStatsDto>> getStats() {
        return ResponseEntity.ok(service.getUniversityStats());
    }

    @GetMapping("/stats/{id}")
    public ResponseEntity<UniversityDetailDto> getUniversityDetail(@PathVariable Integer id) {
        return ResponseEntity.ok(service.getUniversityDetail(id));
    }

    @PostMapping
    public University create(@Valid @RequestBody University entity) {
        return service.save(entity);
    }

    @PutMapping("/{id}")
    public ResponseEntity<University> update(@PathVariable Integer id, @Valid @RequestBody University entity) {
        return service.findById(id)
                .map(existing -> {
                    Optional.ofNullable(entity.getName()).ifPresent(existing::setName);
                    Optional.ofNullable(entity.getShortName()).ifPresent(existing::setShortName);
                    Optional.ofNullable(entity.getCity()).ifPresent(existing::setCity);
                    Optional.ofNullable(entity.getDescription()).ifPresent(existing::setDescription);
                    Optional.ofNullable(entity.getImageLink()).ifPresent(existing::setImageLink);
                    return ResponseEntity.ok(service.save(existing));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        service.findById(id).ifPresentOrElse(
                existing -> service.deleteById(id),
                () -> { throw new RuntimeException("Запись не найдена"); }
        );
        return ResponseEntity.noContent().build();
    }
}