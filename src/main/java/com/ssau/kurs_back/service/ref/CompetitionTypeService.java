package com.ssau.kurs_back.service.ref;

import com.ssau.kurs_back.entity.ref.CompetitionType;
import com.ssau.kurs_back.repository.ref.CompetitionTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CompetitionTypeService {

    private final CompetitionTypeRepository repository;

    public List<CompetitionType> findAll() {
        return repository.findAll();
    }

    public Optional<CompetitionType> findById(Integer id) {
        return repository.findById(id);
    }

    public List<CompetitionType> searchByName(String name) {
        return repository.findByNameContainingIgnoreCase(name);
    }

    @Transactional
    public CompetitionType save(CompetitionType entity) {
        return repository.save(entity);
    }

    @Transactional
    public void deleteById(Integer id) {
        repository.deleteById(id);
    }
}