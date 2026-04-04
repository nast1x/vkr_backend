package com.ssau.kurs_back.repository;

import com.ssau.kurs_back.entity.CompetitionDiscipline;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CompetitionDisciplineRepository extends JpaRepository<CompetitionDiscipline, Integer> {
    List<CompetitionDiscipline> findByCompetitionIdCompetition(Integer competitionId);
    List<CompetitionDiscipline> findByDisciplineTypeIdDisciplineType(Integer disciplineTypeId);
    List<CompetitionDiscipline> findByStageIdStage(Integer stageId);
    List<CompetitionDiscipline> findByCompetitionIdCompetitionAndStageIdStage(Integer competitionId, Integer stageId);

    Optional<CompetitionDiscipline> findByCompetitionIdCompetitionAndDisciplineTypeIdDisciplineTypeAndStageIdStage(
            Integer competitionId, Integer disciplineTypeId, Integer stageId);

    boolean existsByCompetitionIdCompetitionAndDisciplineTypeIdDisciplineTypeAndStageIdStage(
            Integer competitionId, Integer disciplineTypeId, Integer stageId);
}