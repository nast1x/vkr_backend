package com.ssau.kurs_back.dto.competition;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
@JsonPropertyOrder({
        "idCompetition",
        "name",
        "competitionLevel",
        "sportType",
        "city",
        "startDate",
        "endDate"
})
public class CompetitionListDto {
    private Integer idCompetition;
    private String name;      // Название соревнований (organizer)
    private String competitionLevel;     // Уровень проведения (тип соревнования)
    private String sportType;            // Вид спорта
    private String city;                 // Город проведения
    private LocalDate startDate;         // Дата начала
    private LocalDate endDate;           // Дата окончания
}