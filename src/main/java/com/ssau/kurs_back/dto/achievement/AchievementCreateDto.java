package com.ssau.kurs_back.dto.achievement;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class AchievementCreateDto {

    @NotNull(message = "ID спортивного разряда обязателен")
    private Integer sportRankId;

    @NotNull(message = "ID вида спорта обязателен")
    private Integer sportTypeId;

    @NotNull(message = "ID пользователя обязателен")
    private Integer userId;

    @NotNull(message = "Дата получения обязательна")
    private LocalDate dateReceived;

    @Min(value = 0, message = "Стаж не может быть отрицательным")
    private Integer experienceYears;
}