package com.ssau.kurs_back.service.ref;

import com.ssau.kurs_back.entity.ref.SportType;
import com.ssau.kurs_back.repository.ref.SportTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SportTypeService {

    private final SportTypeRepository repository;

    public List<SportType> findAll() {
        return repository.findAll();
    }

    public Optional<SportType> findById(Integer id) {
        return repository.findById(id);
    }

    public List<SportType> searchByName(String name) {
        return repository.findByNameContainingIgnoreCase(name);
    }

    @Transactional
    public SportType save(SportType entity) {
        return repository.save(entity);
    }

    @Transactional
    public void deleteById(Integer id) {
        repository.deleteById(id);
    }
}