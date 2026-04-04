package com.ssau.kurs_back.service;

import com.ssau.kurs_back.dto.result.ResultTeamCreateDto;
import com.ssau.kurs_back.dto.result.ResultTeamResponseDto;
import com.ssau.kurs_back.dto.result.ResultTeamUpdateDto;
import com.ssau.kurs_back.entity.CompetitionDiscipline;
import com.ssau.kurs_back.entity.ResultTeam;
import com.ssau.kurs_back.entity.Team;
import com.ssau.kurs_back.exception.DuplicateResourceException;
import com.ssau.kurs_back.exception.ResourceNotFoundException;
import com.ssau.kurs_back.repository.CompetitionDisciplineRepository;
import com.ssau.kurs_back.repository.ResultTeamRepository;
import com.ssau.kurs_back.repository.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ResultTeamService {

    private final ResultTeamRepository repository;
    private final CompetitionDisciplineRepository disciplineRepository;
    private final TeamRepository teamRepository;

    public List<ResultTeamResponseDto> findAll() {
        return repository.findAll().stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }

    public ResultTeamResponseDto findById(Integer id) {
        ResultTeam result = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Командный результат", id));
        return toResponseDto(result);
    }

    // ✅ Исправлено: метод существует в репозитории
    public List<ResultTeamResponseDto> findByTeamId(Integer teamId) {
        return repository.findByTeamIdTeam(teamId).stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }

    public List<ResultTeamResponseDto> findByDisciplineId(Integer disciplineId) {
        return repository.findByCompetitionDisciplineIdCompetitionDiscipline(disciplineId).stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public ResultTeamResponseDto create(ResultTeamCreateDto dto) {
        CompetitionDiscipline discipline = disciplineRepository.findById(dto.getDisciplineId())
                .orElseThrow(() -> new ResourceNotFoundException("Дисциплина", dto.getDisciplineId()));

        // ✅ Проверка типа участия
        if (!"Team".equals(discipline.getDisciplineType().getParticipationType())) {
            throw new IllegalArgumentException("Дисциплина не является командной");
        }

        // ✅ Исправлено: teamId вместо userId
        Team team = teamRepository.findById(dto.getTeamId())
                .orElseThrow(() -> new ResourceNotFoundException("Команда", dto.getTeamId()));

        // ✅ Исправлено: метод существует в репозитории
        if (repository.existsByCompetitionDisciplineIdCompetitionDisciplineAndTeamIdTeam(
                dto.getDisciplineId(), dto.getTeamId())) {
            throw new DuplicateResourceException("Командный результат",
                    "дисциплина=" + dto.getDisciplineId() + ", команда=" + dto.getTeamId());
        }

        ResultTeam result = ResultTeam.builder()
                .competitionDiscipline(discipline)
                .team(team)
                .rankPlace(dto.getRankPlace())
                .resultValue(dto.getResultValue())
                .build();

        return toResponseDto(repository.save(result));
    }

    @Transactional
    public ResultTeamResponseDto update(Integer id, ResultTeamUpdateDto dto) {
        ResultTeam result = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Командный результат", id));

        if (dto.getRankPlace() != null) result.setRankPlace(dto.getRankPlace());
        if (dto.getResultValue() != null) result.setResultValue(dto.getResultValue());

        return toResponseDto(repository.save(result));
    }

    @Transactional
    public void deleteById(Integer id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Командный результат", id);
        }
        repository.deleteById(id);
    }

    private ResultTeamResponseDto toResponseDto(ResultTeam result) {
        return ResultTeamResponseDto.builder()
                .idDiscipline(result.getCompetitionDiscipline().getIdCompetitionDiscipline())
                .disciplineName(result.getCompetitionDiscipline().getDisciplineName())
                .teamId(result.getTeam().getIdTeam())
                .teamName(result.getTeam().getName())
                .rankPlace(result.getRankPlace())
                .resultValue(result.getResultValue())
                .competitionId(result.getCompetitionDiscipline().getCompetition().getIdCompetition())
                .competitionName(result.getCompetitionDiscipline().getCompetition().getName())
                .build();
    }
}