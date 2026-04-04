package com.ssau.kurs_back.controller.ref;

import com.ssau.kurs_back.dto.sportfacility.SportFacilityDetailDto;
import com.ssau.kurs_back.dto.sportfacility.SportFacilityListDto;
import com.ssau.kurs_back.entity.ref.SportFacility;
import com.ssau.kurs_back.service.ref.SportFacilityService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ref/sport-facilities")
@RequiredArgsConstructor
public class SportFacilityController {

    private final SportFacilityService service;

    @GetMapping
    public ResponseEntity<List<SportFacility>> getAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/list")
    public ResponseEntity<List<SportFacilityListDto>> getSportFacilityList() {
        return ResponseEntity.ok(service.findSportFacilityList());
    }

    @GetMapping("/list/{id}")
    public ResponseEntity<SportFacilityDetailDto> getFacilityDetail(@PathVariable Integer id) {
        return ResponseEntity.ok(service.findFacilityDetail(id));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SportFacility> getById(@PathVariable Integer id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

        @GetMapping("/search")
        public ResponseEntity<List<SportFacility>> searchByCity(@RequestParam String city) {
            return ResponseEntity.ok(service.searchByCity(city));
        }

    @PostMapping
    public ResponseEntity<SportFacility> create(@Valid @RequestBody SportFacility entity) {
        SportFacility created = service.save(entity);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SportFacility> update(@PathVariable Integer id,
                                                @Valid @RequestBody SportFacility entity) {
        return service.findById(id)
                .map(existing -> {
                    if (entity.getName() != null) existing.setName(entity.getName());
                    if (entity.getCity() != null) existing.setCity(entity.getCity());
                    if (entity.getStreet() != null) existing.setStreet(entity.getStreet());
                    if (entity.getStreetNumber() != null) existing.setStreetNumber(entity.getStreetNumber());
                    if (entity.getDescription() != null) existing.setDescription(entity.getDescription());
                    if (entity.getImageLink() != null) existing.setImageLink(entity.getImageLink());
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