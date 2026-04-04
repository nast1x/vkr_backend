package com.ssau.kurs_back.dto.discipline;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CompetitionDisciplineUpdateDto {

    private Integer disciplineTypeId;

    private Integer stageId;

    @Size(max = 255, message = "Название дисциплины слишком длинное")
    private String disciplineName;
}