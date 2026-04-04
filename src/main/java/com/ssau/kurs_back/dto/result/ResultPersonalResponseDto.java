package com.ssau.kurs_back.dto.result;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResultPersonalResponseDto {
    private Integer idDiscipline;
    private String disciplineName;
    private Integer userId;
    private String userName;
    private Integer rankPlace;
    private String resultValue;
    private Integer competitionId;
    private String competitionName;
}