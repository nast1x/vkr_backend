package com.ssau.kurs_back.dto.competition;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class CompetitionResponseDto {
    private Integer idCompetition;
    private String name;
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer competitionTypeId;
    private String competitionTypeName;
    private Integer sportFacilityId;
    private String sportFacilityCity;
    private String sportFacilityStreet;
    private String organizer;
    private Integer sportTypeId;
    private String sportTypeName;
    private String description;
}