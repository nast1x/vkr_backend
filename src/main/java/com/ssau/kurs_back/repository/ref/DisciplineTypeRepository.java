package com.ssau.kurs_back.repository.ref;

import com.ssau.kurs_back.entity.ref.DisciplineType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DisciplineTypeRepository extends JpaRepository<DisciplineType, Integer> {
    List<DisciplineType> findBySportTypeIdSportType(Integer sportTypeId);

    List<DisciplineType> findByParticipantGender(String gender);

    List<DisciplineType> findByParticipationType(String type);

    Optional<DisciplineType> findBySportTypeIdSportTypeAndName(Integer sportTypeId, String name);

    boolean existsBySportTypeIdSportTypeAndName(Integer sportTypeId, String name);

    Optional<DisciplineType> findBySportTypeIdSportTypeAndParticipantGenderAndParticipationType(
            Integer sportTypeId, String gender, String type);

    boolean existsBySportTypeIdSportTypeAndParticipantGenderAndParticipationType(
            Integer sportTypeId, String gender, String type);
}