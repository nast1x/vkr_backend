package com.ssau.kurs_back.dto.discipline;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DisciplineStageResponseDto {
    private Integer idStage;
    private String stageName;
    private String stageNumber;
    private Integer stageOrder;
    private String description;
}