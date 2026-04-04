package com.ssau.kurs_back.service.ref;

import com.ssau.kurs_back.entity.ref.Major;
import com.ssau.kurs_back.repository.ref.MajorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MajorService {

    private final MajorRepository repository;

    public List<Major> findAll() {
        return repository.findAll();
    }

    public Optional<Major> findById(Integer id) {
        return repository.findById(id);
    }

    public List<Major> searchByName(String name) {
        return repository.findByNameContainingIgnoreCase(name);
    }

    @Transactional
    public Major save(Major entity) {
        return repository.save(entity);
    }

    @Transactional
    public void deleteById(Integer id) {
        repository.deleteById(id);
    }
}