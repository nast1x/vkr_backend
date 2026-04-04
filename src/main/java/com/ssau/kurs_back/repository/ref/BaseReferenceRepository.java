package com.ssau.kurs_back.repository.ref;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;

@NoRepositoryBean
public interface BaseReferenceRepository<T> extends JpaRepository<T, Integer> {
    List<T> findByNameContainingIgnoreCase(String name);
}
