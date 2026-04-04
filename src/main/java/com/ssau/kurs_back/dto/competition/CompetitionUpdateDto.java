package com.ssau.kurs_back.dto.competition;

import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

@Data
public class CompetitionUpdateDto {

    @Size(max = 255, message = "Название слишком длинное")
    private String name;

    private LocalDate startDate;

    private LocalDate endDate;

    private Integer competitionTypeId;

    private Integer sportFacilityId;

    @Size(max = 255, message = "Название организатора слишком длинное")
    private String organizer;

    private Integer sportTypeId;

    private String description;
}