package com.ssau.kurs_back.dto.university;

import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data
@Builder
public class UniversityDetailDto {
    private Integer id;
    private String name;
    private String shortName;
    private String city;
    private String imageLink;
    private String description;
    private Integer totalAthletes;
    private Integer totalCoaches;
    private Integer ratingPlace;
    private List<CoachDto> coaches;

    @Data
    @Builder
    public static class CoachDto {
        private Integer id;
        private String fullName;
        private String avatar;
        private String specialization;
        private List<AthleteDto> athletes;
    }

    @Data
    @Builder
    public static class AthleteDto {
        private Integer id;
        private String fullName;
        private String avatar;
        private String sport;
    }
}