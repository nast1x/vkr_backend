package com.ssau.kurs_back.repository.ref;

import com.ssau.kurs_back.entity.ref.DisciplineStage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DisciplineStageRepository extends JpaRepository<DisciplineStage, Integer> {
    Optional<DisciplineStage> findByStageName(String stageName);
    Optional<DisciplineStage> findByStageNumber(String stageNumber);
    boolean existsByStageName(String stageName);
    boolean existsByStageNumber(String stageNumber);
    List<DisciplineStage> findByStageOrderOrderByStageOrderAsc(Integer stageOrder);
    List<DisciplineStage> findAllByOrderByStageOrderAsc();
}