package com.ssau.kurs_back.service.ref;

import com.ssau.kurs_back.entity.ref.SportRank;
import com.ssau.kurs_back.repository.ref.SportRankRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SportRankService {

    private final SportRankRepository repository;

    public List<SportRank> findAll() {
        return repository.findAll();
    }

    public Optional<SportRank> findById(Integer id) {
        return repository.findById(id);
    }

    public List<SportRank> searchByName(String name) {
        return repository.findByNameContainingIgnoreCase(name);
    }

    @Transactional
    public SportRank save(SportRank entity) {
        return repository.save(entity);
    }

    @Transactional
    public void deleteById(Integer id) {
        repository.deleteById(id);
    }
}