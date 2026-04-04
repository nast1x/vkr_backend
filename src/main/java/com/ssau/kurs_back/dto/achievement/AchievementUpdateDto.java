package com.ssau.kurs_back.dto.achievement;

import jakarta.validation.constraints.Min;
import lombok.Data;

import java.time.LocalDate;

@Data
public class AchievementUpdateDto {

    private Integer sportRankId;

    private Integer sportTypeId;

    private LocalDate dateReceived;

    @Min(value = 0, message = "Стаж не может быть отрицательным")
    private Integer experienceYears;
}