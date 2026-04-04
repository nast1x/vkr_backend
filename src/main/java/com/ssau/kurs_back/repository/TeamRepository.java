package com.ssau.kurs_back.repository;

import com.ssau.kurs_back.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TeamRepository extends JpaRepository<Team, Integer> {
    Optional<Team> findByName(String name);
    boolean existsByName(String name);
    List<Team> findBySportTypeIdSportType(Integer sportTypeId);
    List<Team> findByNameContainingIgnoreCase(String name);
}