package com.ssau.kurs_back.service.ref;

import com.ssau.kurs_back.entity.ref.PlayerPosition;
import com.ssau.kurs_back.repository.ref.PlayerPositionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PlayerPositionService {

    private final PlayerPositionRepository repository;

    public List<PlayerPosition> findAll() {
        return repository.findAll();
    }

    public Optional<PlayerPosition> findById(Integer id) {
        return repository.findById(id);
    }

    public List<PlayerPosition> searchByName(String name) {
        return repository.findByNameContainingIgnoreCase(name);
    }

    @Transactional
    public PlayerPosition save(PlayerPosition entity) {
        return repository.save(entity);
    }

    @Transactional
    public void deleteById(Integer id) {
        repository.deleteById(id);
    }
}