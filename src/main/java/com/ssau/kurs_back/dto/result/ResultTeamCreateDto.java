package com.ssau.kurs_back.dto.result;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ResultTeamCreateDto {

    @NotNull(message = "ID дисциплины обязателен")
    private Integer disciplineId;

    @NotNull(message = "ID команды обязателен")
    private Integer teamId;

    @Min(value = 1, message = "Место должно быть больше 0")
    private Integer rankPlace;

    private String resultValue;
}