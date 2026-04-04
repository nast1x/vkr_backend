package com.ssau.kurs_back.dto.statistics;

import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data
@Builder
public class StatisticsDto {
    private GeneralStats generalStats;
    private List<UniversityRankingDto> topUniversities;
    private List<AthleteRankingDto> topAthletes;
    private List<SportStatisticsDto> sportsStatistics;
    private List<CityStatisticsDto> cityStatistics;

    @Data
    @Builder
    public static class GeneralStats {
        private Integer totalAthletes;
        private Integer totalCoaches;
        private Integer totalUniversities;
        private Integer totalCompetitions;
    }

    @Data
    @Builder
    public static class UniversityRankingDto {
        private Integer id;
        private String name;
        private String city;
        private Integer gold;
        private Integer silver;
        private Integer bronze;
        private Integer total;
    }

    @Data
    @Builder
    public static class AthleteRankingDto {
        private Integer id;
        private String name;
        private String avatar;
        private String university;
        private String sport;
        private Integer medals;
        private Integer competitions;
    }

    @Data
    @Builder
    public static class SportStatisticsDto {
        private String name;
        private Integer athletes;
        private Integer competitions;
        private Integer facilities;
        private Double percentage;
    }

    @Data
    @Builder
    public static class CityStatisticsDto {
        private String name;
        private Integer athletes;
        private Integer competitions;
        private Integer total;
    }
}