package com.ssau.kurs_back.service;

import com.ssau.kurs_back.dto.team.TeamMemberCreateDto;
import com.ssau.kurs_back.dto.team.TeamMemberResponseDto;
import com.ssau.kurs_back.dto.team.TeamMemberUpdateDto;
import com.ssau.kurs_back.entity.Team;
import com.ssau.kurs_back.entity.TeamMember;
import com.ssau.kurs_back.entity.TeamMemberId;
import com.ssau.kurs_back.entity.User;
import com.ssau.kurs_back.entity.ref.PlayerPosition;
import com.ssau.kurs_back.exception.DuplicateResourceException;
import com.ssau.kurs_back.exception.ResourceNotFoundException;
import com.ssau.kurs_back.repository.TeamMemberRepository;
import com.ssau.kurs_back.repository.TeamRepository;
import com.ssau.kurs_back.repository.UserRepository;
import com.ssau.kurs_back.repository.ref.PlayerPositionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TeamMemberService {

    private final TeamMemberRepository repository;
    private final TeamRepository teamRepository;
    private final UserRepository userRepository;
    private final PlayerPositionRepository playerPositionRepository;

    public List<TeamMemberResponseDto> findAll() {
        return repository.findAll().stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }

    public TeamMemberResponseDto findById(Integer teamId, Integer userId) {
        TeamMember teamMember = repository.findByTeamIdTeamAndUserIdUser(teamId, userId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Участник команды",
                        "команда=" + teamId + ", пользователь=" + userId
                ));
        return toResponseDto(teamMember);
    }

    public List<TeamMemberResponseDto> findByTeamId(Integer teamId) {
        if (!teamRepository.existsById(teamId)) {
            throw new ResourceNotFoundException("Команда", teamId);
        }
        return repository.findByTeamIdTeam(teamId).stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }

    public List<TeamMemberResponseDto> findByUserId(Integer userId) {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("Пользователь", userId);
        }
        return repository.findByUserIdUser(userId).stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public TeamMemberResponseDto create(TeamMemberCreateDto dto) {
        // Проверка существования команды
        Team team = teamRepository.findById(dto.getTeamId())
                .orElseThrow(() -> new ResourceNotFoundException("Команда", dto.getTeamId()));

        // Проверка существования пользователя
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("Пользователь", dto.getUserId()));

        // Проверка на дубликат (пользователь уже в этой команде)
        if (repository.existsByTeamIdTeamAndUserIdUser(dto.getTeamId(), dto.getUserId())) {
            throw new DuplicateResourceException(
                    "Участник команды",
                    "команда=" + dto.getTeamId() + ", пользователь=" + dto.getUserId()
            );
        }

        // Проверка позиции (опционально)
        PlayerPosition playerPosition = null;
        if (dto.getPlayerPositionId() != null) {
            playerPosition = playerPositionRepository.findById(dto.getPlayerPositionId())
                    .orElseThrow(() -> new ResourceNotFoundException("Позиция игрока", dto.getPlayerPositionId()));
        }

        TeamMember teamMember = TeamMember.builder()
                .team(team)
                .user(user)
                .playerNumber(dto.getPlayerNumber())
                .playerPosition(playerPosition)
                .build();

        return toResponseDto(repository.save(teamMember));
    }

    @Transactional
    public TeamMemberResponseDto update(Integer teamId, Integer userId, TeamMemberUpdateDto dto) {
        TeamMember teamMember = repository.findByTeamIdTeamAndUserIdUser(teamId, userId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Участник команды",
                        "команда=" + teamId + ", пользователь=" + userId
                ));

        if (dto.getPlayerNumber() != null) {
            teamMember.setPlayerNumber(dto.getPlayerNumber());
        }

        if (dto.getPlayerPositionId() != null) {
            PlayerPosition playerPosition = playerPositionRepository.findById(dto.getPlayerPositionId())
                    .orElseThrow(() -> new ResourceNotFoundException("Позиция игрока", dto.getPlayerPositionId()));
            teamMember.setPlayerPosition(playerPosition);
        } else if (dto.getPlayerPositionId() == null) {
            teamMember.setPlayerPosition(null);
        }

        return toResponseDto(repository.save(teamMember));
    }

    @Transactional
    public void deleteById(Integer teamId, Integer userId) {
        if (!repository.existsByTeamIdTeamAndUserIdUser(teamId, userId)) {
            throw new ResourceNotFoundException(
                    "Участник команды",
                    "команда=" + teamId + ", пользователь=" + userId
            );
        }
        repository.deleteById(new TeamMemberId(teamId, userId));
    }

    @Transactional
    public void deleteByUserId(Integer userId) {
        repository.deleteByUserIdUser(userId);
    }

    @Transactional
    public void deleteByTeamId(Integer teamId) {
        repository.deleteByTeamIdTeam(teamId);
    }

    private TeamMemberResponseDto toResponseDto(TeamMember teamMember) {
        return TeamMemberResponseDto.builder()
                .teamId(teamMember.getTeam().getIdTeam())
                .teamName(teamMember.getTeam().getName())
                .userId(teamMember.getUser().getIdUser())
                .userName(teamMember.getUser().getFirstName() + " " + teamMember.getUser().getLastName())
                .playerNumber(teamMember.getPlayerNumber())
                .playerPositionId(teamMember.getPlayerPosition() != null
                        ? teamMember.getPlayerPosition().getIdPlayerPosition()
                        : null)
                .playerPositionName(teamMember.getPlayerPosition() != null
                        ? teamMember.getPlayerPosition().getName()
                        : null)
                .build();
    }
}