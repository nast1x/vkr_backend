package com.ssau.kurs_back.repository;

import com.ssau.kurs_back.entity.TeamMember;
import com.ssau.kurs_back.entity.TeamMemberId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TeamMemberRepository extends JpaRepository<TeamMember, TeamMemberId> {
    List<TeamMember> findByTeamIdTeam(Integer teamId);

    List<TeamMember> findByUserIdUser(Integer userId);

    Optional<TeamMember> findByTeamIdTeamAndUserIdUser(Integer teamId, Integer userId);

    boolean existsByTeamIdTeamAndUserIdUser(Integer teamId, Integer userId);

    void deleteByUserIdUser(Integer userId);

    void deleteByTeamIdTeam(Integer teamId);
}