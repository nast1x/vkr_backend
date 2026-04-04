package com.ssau.kurs_back.repository;

import com.ssau.kurs_back.entity.EducationPlace;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EducationPlaceRepository extends JpaRepository<EducationPlace, Integer> {
    List<EducationPlace> findByUserIdUser(Integer userId);
    List<EducationPlace> findByUniversityIdUniversity(Integer universityId);
    List<EducationPlace> findByMajorIdMajor(Integer majorId);
    Optional<EducationPlace> findByUserIdUserAndUniversityIdUniversity(Integer userId, Integer universityId);
    boolean existsByUserIdUserAndUniversityIdUniversity(Integer userId, Integer universityId);
}