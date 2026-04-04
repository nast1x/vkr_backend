package com.ssau.kurs_back.repository;

import com.ssau.kurs_back.entity.Achievement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AchievementRepository extends JpaRepository<Achievement, Integer> {
    List<Achievement> findByUserIdUser(Integer userId);
    List<Achievement> findBySportRankIdSportRank(Integer sportRankId);
    List<Achievement> findBySportTypeIdSportType(Integer sportTypeId);
    List<Achievement> findByUserIdUserAndSportTypeIdSportType(Integer userId, Integer sportTypeId);
}