package com.ssau.kurs_back.service.ref;

import com.ssau.kurs_back.dto.university.UniversityDetailDto;
import com.ssau.kurs_back.dto.university.UniversityStatsDto;
import com.ssau.kurs_back.entity.ref.University;
import com.ssau.kurs_back.exception.ResourceNotFoundException;
import com.ssau.kurs_back.repository.ref.UniversityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UniversityService {

    private final UniversityRepository repository;

    public List<University> findAll() {
        return repository.findAll();
    }

    public Optional<University> findById(Integer id) {
        return repository.findById(id);
    }

    public List<University> searchByName(String name) {
        return repository.findByNameContainingIgnoreCase(name);
    }

    public List<UniversityStatsDto> getUniversityStats() {
        return repository.getUniversityStats();
    }

    public UniversityDetailDto getUniversityDetail(Integer universityId) {
        // Основная информация
        Object basicInfo_result = repository.findUniversityDetail(universityId)
                .orElseThrow(() -> new ResourceNotFoundException("Вуз", universityId));
        Object[] basicInfo = (Object[]) basicInfo_result;

        // Тренеры со спортсменами

        List<Object[]> coachesData = repository.findCoachesWithAthletes(universityId);

        Map<Integer, UniversityDetailDto.CoachDto> coachMap = new LinkedHashMap<>();

        for (Object[] row : coachesData) {
            Integer coachId = convertToInt(row[0]);

            if (!coachMap.containsKey(coachId)) {
                coachMap.put(coachId, UniversityDetailDto.CoachDto.builder()
                        .id(coachId)
                        .fullName((String) row[1])
                        .avatar(row[2] != null ? (String) row[2] : "/assets/images/avatar-placeholder.png")
                        .specialization((String) row[3])
                        .athletes(new ArrayList<>())
                        .build());
            }

            // Добавляем спортсмена если есть
            if (row[4] != null) {
                UniversityDetailDto.AthleteDto athlete = UniversityDetailDto.AthleteDto.builder()
                        .id(convertToInt(row[4]))
                        .fullName((String) row[5])
                        .avatar(row[6] != null ? (String) row[6] : "/assets/images/avatar-placeholder.png")
                        .sport((String) row[7])
                        .build();

                // Проверка на дубликаты
                boolean exists = coachMap.get(coachId).getAthletes().stream()
                        .anyMatch(a -> a.getId().equals(athlete.getId()));
                if (!exists) {
                    coachMap.get(coachId).getAthletes().add(athlete);
                }
            }
        }

        return UniversityDetailDto.builder()
                .id(convertToInt(basicInfo[0]))
                .name((String) basicInfo[1])
                .shortName((String) basicInfo[2])
                .city((String) basicInfo[3])
                .description((String) basicInfo[4])
                .totalAthletes(convertToInt(basicInfo[5]))
                .totalCoaches(convertToInt(basicInfo[6]))
                .ratingPlace(convertToInt(basicInfo[7]))
                .coaches(new ArrayList<>(coachMap.values()))
                .build();
    }


    @Transactional
    public University save(University entity) {
        return repository.save(entity);
    }

    @Transactional
    public void deleteById(Integer id) {
        repository.deleteById(id);
    }

    private Integer convertToInt(Object value) {
        if (value == null) return 0;
        if (value instanceof Number) return ((Number) value).intValue();
        try {
            return Integer.parseInt(value.toString());
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}