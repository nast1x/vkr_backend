package com.ssau.kurs_back.service;

import com.ssau.kurs_back.dto.discipline.CompetitionDisciplineCreateDto;
import com.ssau.kurs_back.dto.discipline.CompetitionDisciplineResponseDto;
import com.ssau.kurs_back.dto.discipline.CompetitionDisciplineUpdateDto;
import com.ssau.kurs_back.entity.Competition;
import com.ssau.kurs_back.entity.CompetitionDiscipline;
import com.ssau.kurs_back.entity.ref.DisciplineStage;
import com.ssau.kurs_back.entity.ref.DisciplineType;
import com.ssau.kurs_back.exception.DuplicateResourceException;
import com.ssau.kurs_back.exception.ResourceNotFoundException;
import com.ssau.kurs_back.repository.CompetitionDisciplineRepository;
import com.ssau.kurs_back.repository.CompetitionRepository;
import com.ssau.kurs_back.repository.ref.DisciplineStageRepository;
import com.ssau.kurs_back.repository.ref.DisciplineTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CompetitionDisciplineService {

    private final CompetitionDisciplineRepository repository;
    private final CompetitionRepository competitionRepository;
    private final DisciplineTypeRepository disciplineTypeRepository;
    private final DisciplineStageRepository disciplineStageRepository;

    public List<CompetitionDisciplineResponseDto> findAll() {
        return repository.findAll().stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }

    public CompetitionDisciplineResponseDto findById(Integer id) {
        CompetitionDiscipline discipline = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Дисциплина соревнования", id));
        return toResponseDto(discipline);
    }

    public List<CompetitionDisciplineResponseDto> findByCompetitionId(Integer competitionId) {
        return repository.findByCompetitionIdCompetition(competitionId).stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }

    public List<CompetitionDisciplineResponseDto> findByDisciplineTypeId(Integer disciplineTypeId) {
        return repository.findByDisciplineTypeIdDisciplineType(disciplineTypeId).stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }

    public List<CompetitionDisciplineResponseDto> findByStageId(Integer stageId) {
        return repository.findByStageIdStage(stageId).stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public CompetitionDisciplineResponseDto create(CompetitionDisciplineCreateDto dto) {
        Competition competition = competitionRepository.findById(dto.getCompetitionId())
                .orElseThrow(() -> new ResourceNotFoundException("Соревнование", dto.getCompetitionId()));

        DisciplineType disciplineType = disciplineTypeRepository.findById(dto.getDisciplineTypeId())
                .orElseThrow(() -> new ResourceNotFoundException("Тип дисциплины", dto.getDisciplineTypeId()));

        DisciplineStage stage = disciplineStageRepository.findById(dto.getStageId())
                .orElseThrow(() -> new ResourceNotFoundException("Этап", dto.getStageId()));

        // Проверка уникальности
        if (repository.existsByCompetitionIdCompetitionAndDisciplineTypeIdDisciplineTypeAndStageIdStage(
                dto.getCompetitionId(), dto.getDisciplineTypeId(), dto.getStageId())) {
            throw new DuplicateResourceException("Дисциплина уже существует для этого соревнования",
                    "соревнование=" + dto.getCompetitionId() + ", тип=" + dto.getDisciplineTypeId() +
                            ", этап=" + dto.getStageId());
        }

        CompetitionDiscipline discipline = CompetitionDiscipline.builder()
                .competition(competition)
                .disciplineType(disciplineType)
                .stage(stage)
                .disciplineName(dto.getDisciplineName())
                .build();

        return toResponseDto(repository.save(discipline));
    }

    @Transactional
    public CompetitionDisciplineResponseDto update(Integer id, CompetitionDisciplineUpdateDto dto) {
        CompetitionDiscipline discipline = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Дисциплина соревнования", id));

        if (dto.getDisciplineTypeId() != null && !dto.getDisciplineTypeId().equals(
                discipline.getDisciplineType().getIdDisciplineType())) {
            DisciplineType disciplineType = disciplineTypeRepository.findById(dto.getDisciplineTypeId())
                    .orElseThrow(() -> new ResourceNotFoundException("Тип дисциплины", dto.getDisciplineTypeId()));

            if (repository.existsByCompetitionIdCompetitionAndDisciplineTypeIdDisciplineTypeAndStageIdStage(
                    discipline.getCompetition().getIdCompetition(), dto.getDisciplineTypeId(),
                    discipline.getStage().getIdStage())) {
                throw new DuplicateResourceException("Дисциплина уже существует",
                        "тип=" + dto.getDisciplineTypeId());
            }
            discipline.setDisciplineType(disciplineType);
        }

        if (dto.getStageId() != null && !dto.getStageId().equals(discipline.getStage().getIdStage())) {
            DisciplineStage stage = disciplineStageRepository.findById(dto.getStageId())
                    .orElseThrow(() -> new ResourceNotFoundException("Этап", dto.getStageId()));

            if (repository.existsByCompetitionIdCompetitionAndDisciplineTypeIdDisciplineTypeAndStageIdStage(
                    discipline.getCompetition().getIdCompetition(),
                    discipline.getDisciplineType().getIdDisciplineType(), dto.getStageId())) {
                throw new DuplicateResourceException("Дисциплина уже существует",
                        "этап=" + dto.getStageId());
            }
            discipline.setStage(stage);
        }

        if (dto.getDisciplineName() != null) {
            discipline.setDisciplineName(dto.getDisciplineName());
        }

        return toResponseDto(repository.save(discipline));
    }

    @Transactional
    public void deleteById(Integer id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Дисциплина соревнования", id);
        }
        repository.deleteById(id);
    }

    private CompetitionDisciplineResponseDto toResponseDto(CompetitionDiscipline discipline) {
        return CompetitionDisciplineResponseDto.builder()
                .idCompetitionDiscipline(discipline.getIdCompetitionDiscipline())
                .disciplineTypeId(discipline.getDisciplineType().getIdDisciplineType())
                .disciplineTypeName(discipline.getDisciplineType().getName())
                .sportTypeId(discipline.getDisciplineType().getSportType().getIdSportType())
                .sportTypeName(discipline.getDisciplineType().getSportType().getName())
                .competitionId(discipline.getCompetition().getIdCompetition())
                .competitionName(discipline.getCompetition().getName())
                .stageId(discipline.getStage().getIdStage())
                .stageName(discipline.getStage().getStageName())
                .stageNumber(discipline.getStage().getStageNumber())
                .disciplineName(discipline.getDisciplineName())
                .participantGender(discipline.getDisciplineType().getParticipantGender())
                .participationType(discipline.getDisciplineType().getParticipationType())
                .build();
    }
}