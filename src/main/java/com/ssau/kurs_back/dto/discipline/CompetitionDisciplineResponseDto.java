package com.ssau.kurs_back.dto.discipline;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CompetitionDisciplineResponseDto {
    private Integer idCompetitionDiscipline;
    private Integer disciplineTypeId;
    private String disciplineTypeName;
    private Integer sportTypeId;
    private String sportTypeName;
    private Integer competitionId;
    private String competitionName;
    private Integer stageId;
    private String stageName;
    private String stageNumber;
    private String disciplineName;
    private String participantGender;
    private String participationType;
}