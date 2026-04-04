package com.ssau.kurs_back.controller.ref;

import com.ssau.kurs_back.entity.ref.PlayerPosition;
import com.ssau.kurs_back.service.ref.PlayerPositionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ref/player-position")
@RequiredArgsConstructor
public class PlayerPositionController {

    private final PlayerPositionService service;

    @GetMapping
    public List<PlayerPosition> getAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PlayerPosition> getById(@PathVariable Integer id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/search")
    public List<PlayerPosition> search(@RequestParam String name) {
        return service.searchByName(name);
    }

    @PostMapping
    public PlayerPosition create(@Valid @RequestBody PlayerPosition entity) {
        return service.save(entity);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PlayerPosition> update(@PathVariable Integer id, @Valid @RequestBody PlayerPosition entity) {
        return service.findById(id)
                .map(existing -> {
                    existing.setName(entity.getName());
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