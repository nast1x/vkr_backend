package com.ssau.kurs_back.service.ref;

import com.ssau.kurs_back.dto.discipline.DisciplineTypeCreateDto;
import com.ssau.kurs_back.dto.discipline.DisciplineTypeResponseDto;
import com.ssau.kurs_back.dto.discipline.DisciplineTypeUpdateDto;
import com.ssau.kurs_back.entity.ref.DisciplineType;
import com.ssau.kurs_back.entity.ref.SportType;
import com.ssau.kurs_back.exception.DuplicateResourceException;
import com.ssau.kurs_back.exception.ResourceNotFoundException;
import com.ssau.kurs_back.repository.ref.DisciplineTypeRepository;
import com.ssau.kurs_back.repository.ref.SportTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DisciplineTypeService {

    private final DisciplineTypeRepository repository;
    private final SportTypeRepository sportTypeRepository;

    public List<DisciplineTypeResponseDto> findAll() {
        return repository.findAll().stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }

    public DisciplineTypeResponseDto findById(Integer id) {
        DisciplineType type = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Тип дисциплины", id));
        return toResponseDto(type);
    }

    public List<DisciplineTypeResponseDto> findBySportTypeId(Integer sportTypeId) {
        return repository.findBySportTypeIdSportType(sportTypeId).stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }

    public List<DisciplineTypeResponseDto> findByGender(String gender) {
        return repository.findByParticipantGender(gender).stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }

    public List<DisciplineTypeResponseDto> findByParticipationType(String type) {
        return repository.findByParticipationType(type).stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public DisciplineTypeResponseDto create(DisciplineTypeCreateDto dto) {
        SportType sportType = sportTypeRepository.findById(dto.getSportTypeId())
                .orElseThrow(() -> new ResourceNotFoundException("Вид спорта", dto.getSportTypeId()));

        // ✅ Проверка уникальности имени в рамках вида спорта
        if (repository.existsBySportTypeIdSportTypeAndName(dto.getSportTypeId(), dto.getName())) {
            throw new DuplicateResourceException("Тип дисциплины с таким названием уже существует для этого вида спорта",
                    dto.getName());
        }

        // ✅ Проверка уникальности комбинации (sport, gender, participation)
        if (repository.existsBySportTypeIdSportTypeAndParticipantGenderAndParticipationType(
                dto.getSportTypeId(), dto.getParticipantGender(), dto.getParticipationType())) {
            throw new DuplicateResourceException("Тип дисциплины уже существует",
                    "вид спорта=" + dto.getSportTypeId() + ", пол=" + dto.getParticipantGender() +
                            ", тип=" + dto.getParticipationType());
        }

        DisciplineType type = DisciplineType.builder()
                .sportType(sportType)
                .name(dto.getName())
                .participantGender(dto.getParticipantGender())
                .participationType(dto.getParticipationType())
                .description(dto.getDescription())
                .build();

        return toResponseDto(repository.save(type));
    }

    @Transactional
    public DisciplineTypeResponseDto update(Integer id, DisciplineTypeUpdateDto dto) {
        DisciplineType type = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Тип дисциплины", id));

        // Проверка вида спорта при изменении
        if (dto.getSportTypeId() != null && !dto.getSportTypeId().equals(type.getSportType().getIdSportType())) {
            SportType sportType = sportTypeRepository.findById(dto.getSportTypeId())
                    .orElseThrow(() -> new ResourceNotFoundException("Вид спорта", dto.getSportTypeId()));

            if (repository.existsBySportTypeIdSportTypeAndName(dto.getSportTypeId(), type.getName())) {
                throw new DuplicateResourceException("Тип дисциплины с таким названием уже существует",
                        type.getName());
            }
            type.setSportType(sportType);
        }

        if (dto.getName() != null && !dto.getName().equals(type.getName())) {
            if (repository.existsBySportTypeIdSportTypeAndName(type.getSportType().getIdSportType(), dto.getName())) {
                throw new DuplicateResourceException("Тип дисциплины с таким названием уже существует",
                        dto.getName());
            }
            type.setName(dto.getName());
        }

        // Проверка пола при изменении
        if (dto.getParticipantGender() != null && !dto.getParticipantGender().equals(type.getParticipantGender())) {
            if (repository.existsBySportTypeIdSportTypeAndParticipantGenderAndParticipationType(
                    type.getSportType().getIdSportType(), dto.getParticipantGender(), type.getParticipationType())) {
                throw new DuplicateResourceException("Тип дисциплины уже существует",
                        "пол=" + dto.getParticipantGender());
            }
            type.setParticipantGender(dto.getParticipantGender());
        }

        // Проверка типа участия при изменении
        if (dto.getParticipationType() != null && !dto.getParticipationType().equals(type.getParticipationType())) {
            if (repository.existsBySportTypeIdSportTypeAndParticipantGenderAndParticipationType(
                    type.getSportType().getIdSportType(), type.getParticipantGender(), dto.getParticipationType())) {
                throw new DuplicateResourceException("Тип дисциплины уже существует",
                        "тип=" + dto.getParticipationType());
            }
            type.setParticipationType(dto.getParticipationType());
        }

        if (dto.getDescription() != null) {
            type.setDescription(dto.getDescription());
        }

        return toResponseDto(repository.save(type));
    }

    @Transactional
    public void deleteById(Integer id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Тип дисциплины", id);
        }
        repository.deleteById(id);
    }

    private DisciplineTypeResponseDto toResponseDto(DisciplineType type) {
        return DisciplineTypeResponseDto.builder()
                .idDisciplineType(type.getIdDisciplineType())
                .sportTypeId(type.getSportType().getIdSportType())
                .sportTypeName(type.getSportType().getName())
                .name(type.getName())
                .participantGender(type.getParticipantGender())
                .participationType(type.getParticipationType())
                .description(type.getDescription())
                .build();
    }
}