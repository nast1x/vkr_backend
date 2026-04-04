package com.ssau.kurs_back.dto.result;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResultTeamResponseDto {
    private Integer idDiscipline;
    private String disciplineName;
    private Integer teamId;
    private String teamName;
    private Integer rankPlace;
    private String resultValue;
    private Integer competitionId;
    private String competitionName;
}