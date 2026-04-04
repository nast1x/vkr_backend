package com.ssau.kurs_back.dto.team;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TeamMemberResponseDto {
    private Integer teamId;
    private String teamName;
    private Integer userId;
    private String userName;
    private Integer playerNumber;
    private Integer playerPositionId;
    private String playerPositionName;
}