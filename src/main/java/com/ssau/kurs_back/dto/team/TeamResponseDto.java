package com.ssau.kurs_back.dto.team;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TeamResponseDto {
    private Integer idTeam;
    private String name;
    private Integer sportTypeId;
    private String sportTypeName;
}