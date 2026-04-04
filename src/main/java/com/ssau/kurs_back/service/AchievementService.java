package com.ssau.kurs_back.service;

import com.ssau.kurs_back.dto.achievement.AchievementCreateDto;
import com.ssau.kurs_back.dto.achievement.AchievementResponseDto;
import com.ssau.kurs_back.dto.achievement.AchievementUpdateDto;
import com.ssau.kurs_back.entity.Achievement;
import com.ssau.kurs_back.entity.User;
import com.ssau.kurs_back.entity.ref.SportRank;
import com.ssau.kurs_back.entity.ref.SportType;
import com.ssau.kurs_back.repository.AchievementRepository;
import com.ssau.kurs_back.repository.UserRepository;
import com.ssau.kurs_back.repository.ref.SportRankRepository;
import com.ssau.kurs_back.repository.ref.SportTypeRepository;
import com.ssau.kurs_back.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AchievementService {

    private final AchievementRepository repository;
    private final UserRepository userRepository;
    private final SportRankRepository sportRankRepository;
    private final SportTypeRepository sportTypeRepository;

    public List<AchievementResponseDto> findAll() {
        return repository.findAll().stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }

    public AchievementResponseDto findById(Integer id) {
        Achievement achievement = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Достижение", id));
        return toResponseDto(achievement);
    }

    public List<AchievementResponseDto> findByUserId(Integer userId) {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("Пользователь", userId);
        }
        return repository.findByUserIdUser(userId).stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }

    public List<AchievementResponseDto> findBySportRankId(Integer sportRankId) {
        return repository.findBySportRankIdSportRank(sportRankId).stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }

    public List<AchievementResponseDto> findBySportTypeId(Integer sportTypeId) {
        return repository.findBySportTypeIdSportType(sportTypeId).stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public AchievementResponseDto create(AchievementCreateDto dto) {
        SportRank sportRank = sportRankRepository.findById(dto.getSportRankId())
                .orElseThrow(() -> new ResourceNotFoundException("Спортивный разряд", dto.getSportRankId()));

        SportType sportType = sportTypeRepository.findById(dto.getSportTypeId())
                .orElseThrow(() -> new ResourceNotFoundException("Вид спорта", dto.getSportTypeId()));

        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("Пользователь", dto.getUserId()));

        Achievement achievement = Achievement.builder()
                .sportRank(sportRank)
                .sportType(sportType)
                .user(user)
                .dateReceived(dto.getDateReceived())
                .experienceYears(dto.getExperienceYears())
                .build();

        return toResponseDto(repository.save(achievement));
    }

    @Transactional
    public AchievementResponseDto update(Integer id, AchievementUpdateDto dto) {
        Achievement achievement = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Достижение", id));

        if (dto.getSportRankId() != null) {
            SportRank sportRank = sportRankRepository.findById(dto.getSportRankId())
                    .orElseThrow(() -> new ResourceNotFoundException("Спортивный разряд", dto.getSportRankId()));
            achievement.setSportRank(sportRank);
        }

        if (dto.getSportTypeId() != null) {
            SportType sportType = sportTypeRepository.findById(dto.getSportTypeId())
                    .orElseThrow(() -> new ResourceNotFoundException("Вид спорта", dto.getSportTypeId()));
            achievement.setSportType(sportType);
        }

        if (dto.getDateReceived() != null) {
            achievement.setDateReceived(dto.getDateReceived());
        }

        if (dto.getExperienceYears() != null) {
            achievement.setExperienceYears(dto.getExperienceYears());
        }

        return toResponseDto(repository.save(achievement));
    }

    @Transactional
    public void deleteById(Integer id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Достижение", id);
        }
        repository.deleteById(id);
    }

    private AchievementResponseDto toResponseDto(Achievement achievement) {
        return AchievementResponseDto.builder()
                .idAchievement(achievement.getIdAchievement())
                .sportRankId(achievement.getSportRank().getIdSportRank())
                .sportRankName(achievement.getSportRank().getName())
                .sportTypeId(achievement.getSportType().getIdSportType())
                .sportTypeName(achievement.getSportType().getName())
                .userId(achievement.getUser().getIdUser())
                .userName(achievement.getUser().getFirstName() + " " + achievement.getUser().getLastName())
                .dateReceived(achievement.getDateReceived())
                .experienceYears(achievement.getExperienceYears())
                .build();
    }
}