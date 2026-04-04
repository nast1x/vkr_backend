package com.ssau.kurs_back.dto.discipline;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CompetitionDisciplineCreateDto {

    @NotNull(message = "ID типа дисциплины обязателен")
    private Integer disciplineTypeId;

    @NotNull(message = "ID соревнования обязательно")
    private Integer competitionId;

    @NotNull(message = "ID этапа обязателен")
    private Integer stageId;

    @NotBlank(message = "Название дисциплины обязательно")
    @Size(max = 255, message = "Название дисциплины слишком длинное")
    private String disciplineName;
}