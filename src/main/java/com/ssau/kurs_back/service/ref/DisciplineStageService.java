package com.ssau.kurs_back.service.ref;


import com.ssau.kurs_back.dto.discipline.DisciplineStageCreateDto;
import com.ssau.kurs_back.dto.discipline.DisciplineStageResponseDto;
import com.ssau.kurs_back.dto.discipline.DisciplineStageUpdateDto;
import com.ssau.kurs_back.entity.ref.DisciplineStage;
import com.ssau.kurs_back.repository.ref.DisciplineStageRepository;
import com.ssau.kurs_back.exception.ResourceNotFoundException;
import com.ssau.kurs_back.exception.DuplicateResourceException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DisciplineStageService {

    private final DisciplineStageRepository repository;

    public List<DisciplineStageResponseDto> findAll() {
        return repository.findAllByOrderByStageOrderAsc().stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }

    public DisciplineStageResponseDto findById(Integer id) {
        DisciplineStage stage = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Этап дисциплины", id));
        return toResponseDto(stage);
    }

    @Transactional
    public DisciplineStageResponseDto create(DisciplineStageCreateDto dto) {
        if (repository.existsByStageName(dto.getStageName())) {
            throw new DuplicateResourceException("Этап с таким названием уже существует", dto.getStageName());
        }

        if (dto.getStageNumber() != null && repository.existsByStageNumber(dto.getStageNumber())) {
            throw new DuplicateResourceException("Этап с таким номером уже существует", dto.getStageNumber());
        }

        DisciplineStage stage = DisciplineStage.builder()
                .stageName(dto.getStageName())
                .stageNumber(dto.getStageNumber())
                .stageOrder(dto.getStageOrder() != null ? dto.getStageOrder() : 0)
                .description(dto.getDescription())
                .build();

        return toResponseDto(repository.save(stage));
    }

    @Transactional
    public DisciplineStageResponseDto update(Integer id, DisciplineStageUpdateDto dto) {
        DisciplineStage stage = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Этап дисциплины", id));

        // Проверка уникальности названия при изменении
        if (dto.getStageName() != null && !dto.getStageName().equals(stage.getStageName())) {
            if (repository.existsByStageName(dto.getStageName())) {
                throw new DuplicateResourceException("Этап с таким названием уже существует", dto.getStageName());
            }
            stage.setStageName(dto.getStageName());
        }

        // Проверка уникальности номера при изменении
        if (dto.getStageNumber() != null && !dto.getStageNumber().equals(stage.getStageNumber())) {
            if (repository.existsByStageNumber(dto.getStageNumber())) {
                throw new DuplicateResourceException("Этап с таким номером уже существует", dto.getStageNumber());
            }
            stage.setStageNumber(dto.getStageNumber());
        }

        if (dto.getStageOrder() != null) {
            stage.setStageOrder(dto.getStageOrder());
        }

        if (dto.getDescription() != null) {
            stage.setDescription(dto.getDescription());
        }

        return toResponseDto(repository.save(stage));
    }

    @Transactional
    public void deleteById(Integer id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Этап дисциплины", id);
        }
        repository.deleteById(id);
    }

    private DisciplineStageResponseDto toResponseDto(DisciplineStage stage) {
        return DisciplineStageResponseDto.builder()
                .idStage(stage.getIdStage())
                .stageName(stage.getStageName())
                .stageNumber(stage.getStageNumber())
                .stageOrder(stage.getStageOrder())
                .description(stage.getDescription())
                .build();
    }
}