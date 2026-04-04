package com.ssau.kurs_back.service;

import com.ssau.kurs_back.dto.result.ResultPersonalCreateDto;
import com.ssau.kurs_back.dto.result.ResultPersonalResponseDto;
import com.ssau.kurs_back.dto.result.ResultPersonalUpdateDto;
import com.ssau.kurs_back.entity.CompetitionDiscipline;
import com.ssau.kurs_back.entity.ResultPersonal;
import com.ssau.kurs_back.entity.User;
import com.ssau.kurs_back.exception.DuplicateResourceException;
import com.ssau.kurs_back.exception.ResourceNotFoundException;
import com.ssau.kurs_back.repository.CompetitionDisciplineRepository;
import com.ssau.kurs_back.repository.ResultPersonalRepository;
import com.ssau.kurs_back.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ResultPersonalService {

    private final ResultPersonalRepository repository;
    private final CompetitionDisciplineRepository disciplineRepository;
    private final UserRepository userRepository;

    public List<ResultPersonalResponseDto> findAll() {
        return repository.findAll().stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }

    public ResultPersonalResponseDto findById(Integer id) {
        ResultPersonal result = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Личный результат", id));
        return toResponseDto(result);
    }

    // ✅ Исправлено: метод существует в репозитории
    public List<ResultPersonalResponseDto> findByUserId(Integer userId) {
        return repository.findByUserIdUser(userId).stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }

    public List<ResultPersonalResponseDto> findByDisciplineId(Integer disciplineId) {
        return repository.findByCompetitionDisciplineIdCompetitionDiscipline(disciplineId).stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public ResultPersonalResponseDto create(ResultPersonalCreateDto dto) {
        CompetitionDiscipline discipline = disciplineRepository.findById(dto.getDisciplineId())
                .orElseThrow(() -> new ResourceNotFoundException("Дисциплина", dto.getDisciplineId()));

        // ✅ Проверка типа участия
        if (!"Personal".equals(discipline.getDisciplineType().getParticipationType())) {
            throw new IllegalArgumentException("Дисциплина не является личной");
        }

        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("Пользователь", dto.getUserId()));

        // ✅ Исправлено: метод существует в репозитории
        if (repository.existsByCompetitionDisciplineIdCompetitionDisciplineAndUserIdUser(
                dto.getDisciplineId(), dto.getUserId())) {
            throw new DuplicateResourceException("Личный результат",
                    "дисциплина=" + dto.getDisciplineId() + ", пользователь=" + dto.getUserId());
        }

        ResultPersonal result = ResultPersonal.builder()
                .competitionDiscipline(discipline)
                .user(user)
                .rankPlace(dto.getRankPlace())
                .resultValue(dto.getResultValue())
                .build();

        return toResponseDto(repository.save(result));
    }

    @Transactional
    public ResultPersonalResponseDto update(Integer id, ResultPersonalUpdateDto dto) {
        ResultPersonal result = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Личный результат", id));

        if (dto.getRankPlace() != null) result.setRankPlace(dto.getRankPlace());
        if (dto.getResultValue() != null) result.setResultValue(dto.getResultValue());

        return toResponseDto(repository.save(result));
    }

    @Transactional
    public void deleteById(Integer id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Личный результат", id);
        }
        repository.deleteById(id);
    }

    private ResultPersonalResponseDto toResponseDto(ResultPersonal result) {
        return ResultPersonalResponseDto.builder()
                .idDiscipline(result.getCompetitionDiscipline().getIdCompetitionDiscipline())
                .disciplineName(result.getCompetitionDiscipline().getDisciplineName())
                .userId(result.getUser().getIdUser())
                .userName(result.getUser().getFirstName() + " " + result.getUser().getLastName())
                .rankPlace(result.getRankPlace())
                .resultValue(result.getResultValue())
                .competitionId(result.getCompetitionDiscipline().getCompetition().getIdCompetition())
                .competitionName(result.getCompetitionDiscipline().getCompetition().getName())
                .build();
    }
}