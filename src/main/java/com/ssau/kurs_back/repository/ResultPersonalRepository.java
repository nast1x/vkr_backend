package com.ssau.kurs_back.repository;

import com.ssau.kurs_back.entity.ResultPersonal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ResultPersonalRepository extends JpaRepository<ResultPersonal, Integer> {

    // ✅ Поиск по пользователю
    List<ResultPersonal> findByUserIdUser(Integer userId);

    // ✅ Поиск по дисциплине
    List<ResultPersonal> findByCompetitionDisciplineIdCompetitionDiscipline(Integer competitionDisciplineId);

    // ✅ Поиск по дисциплине и пользователю
    Optional<ResultPersonal> findByCompetitionDisciplineIdCompetitionDisciplineAndUserIdUser(
            Integer competitionDisciplineId, Integer userId);

    // ✅ Проверка на дубликат
    boolean existsByCompetitionDisciplineIdCompetitionDisciplineAndUserIdUser(
            Integer competitionDisciplineId, Integer userId);
}