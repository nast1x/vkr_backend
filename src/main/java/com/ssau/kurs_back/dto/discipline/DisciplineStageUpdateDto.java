package com.ssau.kurs_back.dto.discipline;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class DisciplineStageUpdateDto {

    @Size(max = 100, message = "Название этапа слишком длинное")
    private String stageName;

    @Size(max = 20, message = "Номер этапа слишком длинный")
    private String stageNumber;

    private Integer stageOrder;

    private String description;
}