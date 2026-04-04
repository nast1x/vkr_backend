package com.ssau.kurs_back.service.ref;

import com.ssau.kurs_back.entity.ref.Role;
import com.ssau.kurs_back.repository.ref.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RoleService {

    private final RoleRepository repository;

    public List<Role> findAll() {
        return repository.findAll();
    }

    public Optional<Role> findById(Integer id) {
        return repository.findById(id);
    }

    public List<Role> searchByName(String name) {
        return repository.findByNameContainingIgnoreCase(name);
    }

    @Transactional
    public Role save(Role entity) {
        return repository.save(entity);
    }

    @Transactional
    public void deleteById(Integer id) {
        repository.deleteById(id);
    }
}