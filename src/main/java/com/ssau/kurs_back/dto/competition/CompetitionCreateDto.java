package com.ssau.kurs_back.dto.competition;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

@Data
public class CompetitionCreateDto {

    @NotBlank(message = "Название соревнования обязательно")
    @Size(max = 255, message = "Название слишком длинное")
    private String name;

    @NotNull(message = "Дата начала обязательна")
    private LocalDate startDate;

    private LocalDate endDate;

    @NotNull(message = "ID типа соревнования обязателен")
    private Integer competitionTypeId;

    @NotNull(message = "ID спортивного объекта обязателен")
    private Integer sportFacilityId;

    @NotBlank(message = "Организатор обязателен")
    @Size(max = 255, message = "Название организатора слишком длинное")
    private String organizer;

    @NotNull(message = "ID вида спорта обязателен")
    private Integer sportTypeId;

    private String description;
}