package com.ssau.kurs_back.controller.ref;

import com.ssau.kurs_back.entity.ref.Major;
import com.ssau.kurs_back.service.ref.MajorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ref/major")
@RequiredArgsConstructor
public class MajorController {

    private final MajorService service;

    @GetMapping
    public List<Major> getAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Major> getById(@PathVariable Integer id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/search")
    public List<Major> search(@RequestParam String name) {
        return service.searchByName(name);
    }

    @PostMapping
    public Major create(@Valid @RequestBody Major entity) {
        return service.save(entity);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Major> update(@PathVariable Integer id, @Valid @RequestBody Major entity) {
        return service.findById(id)
                .map(existing -> {
                    existing.setName(entity.getName());
                    existing.setCode(entity.getCode());
                    existing.setDescription(entity.getDescription());
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