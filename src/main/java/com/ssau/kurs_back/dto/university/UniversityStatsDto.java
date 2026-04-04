package com.ssau.kurs_back.dto.university;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({
        "idUniversity",
        "shortName",
        "city",
        "athletesCount",
        "coachesCount"
})
public class UniversityStatsDto {
    private Integer idUniversity;
    private String shortName;
    private String city;
    private Integer athletesCount;
    private Integer coachesCount;
}