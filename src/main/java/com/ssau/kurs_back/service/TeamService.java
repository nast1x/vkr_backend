package com.ssau.kurs_back.service;

import com.ssau.kurs_back.dto.team.TeamCreateDto;
import com.ssau.kurs_back.dto.team.TeamResponseDto;
import com.ssau.kurs_back.dto.team.TeamUpdateDto;
import com.ssau.kurs_back.entity.Team;
import com.ssau.kurs_back.entity.ref.SportType;
import com.ssau.kurs_back.repository.TeamRepository;
import com.ssau.kurs_back.repository.ref.SportTypeRepository;
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
public class TeamService {

    private final TeamRepository repository;
    private final SportTypeRepository sportTypeRepository;

    public List<TeamResponseDto> findAll() {
        return repository.findAll().stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }

    public TeamResponseDto findById(Integer id) {
        Team team = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Команда", id));
        return toResponseDto(team);
    }

    public TeamResponseDto findByName(String name) {
        Team team = repository.findByName(name)
                .orElseThrow(() -> new ResourceNotFoundException("Команда", name));
        return toResponseDto(team);
    }

    public List<TeamResponseDto> findBySportTypeId(Integer sportTypeId) {
        return repository.findBySportTypeIdSportType(sportTypeId).stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }

    public List<TeamResponseDto> searchByName(String name) {
        return repository.findByNameContainingIgnoreCase(name).stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public TeamResponseDto create(TeamCreateDto dto) {
        if (repository.existsByName(dto.getName())) {
            throw new DuplicateResourceException("Команда с таким названием уже существует", dto.getName());
        }

        SportType sportType = sportTypeRepository.findById(dto.getSportTypeId())
                .orElseThrow(() -> new ResourceNotFoundException("Вид спорта", dto.getSportTypeId()));

        Team team = Team.builder()
                .name(dto.getName())
                .sportType(sportType)
                .build();

        return toResponseDto(repository.save(team));
    }

    @Transactional
    public TeamResponseDto update(Integer id, TeamUpdateDto dto) {
        Team team = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Команда", id));

        if (dto.getName() != null && !dto.getName().equals(team.getName())) {
            if (repository.existsByName(dto.getName())) {
                throw new DuplicateResourceException("Команда с таким названием уже существует", dto.getName());
            }
            team.setName(dto.getName());
        }

        if (dto.getSportTypeId() != null) {
            SportType sportType = sportTypeRepository.findById(dto.getSportTypeId())
                    .orElseThrow(() -> new ResourceNotFoundException("Вид спорта", dto.getSportTypeId()));
            team.setSportType(sportType);
        }

        return toResponseDto(repository.save(team));
    }

    @Transactional
    public void deleteById(Integer id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Команда", id);
        }
        repository.deleteById(id);
    }

    private TeamResponseDto toResponseDto(Team team) {
        return TeamResponseDto.builder()
                .idTeam(team.getIdTeam())
                .name(team.getName())
                .sportTypeId(team.getSportType().getIdSportType())
                .sportTypeName(team.getSportType().getName())
                .build();
    }
}