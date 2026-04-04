package com.ssau.kurs_back.repository;

import com.ssau.kurs_back.entity.ResultTeam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ResultTeamRepository extends JpaRepository<ResultTeam, Integer> {

    // ✅ ИСПРАВЛЕНО: competitionDiscipline вместо discipline
    List<ResultTeam> findByCompetitionDisciplineIdCompetitionDiscipline(Integer competitionDisciplineId);

    List<ResultTeam> findByTeamIdTeam(Integer teamId);

    Optional<ResultTeam> findByCompetitionDisciplineIdCompetitionDisciplineAndTeamIdTeam(
            Integer competitionDisciplineId, Integer teamId);

    boolean existsByCompetitionDisciplineIdCompetitionDisciplineAndTeamIdTeam(
            Integer competitionDisciplineId, Integer teamId);
}