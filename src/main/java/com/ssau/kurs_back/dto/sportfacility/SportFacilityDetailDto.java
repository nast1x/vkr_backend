package com.ssau.kurs_back.dto.sportfacility;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SportFacilityDetailDto {
    private Integer id;
    private String name;
    private String city;
    private String address;
    private String photo;
    private String description;
    private List<String> sportsList;
    private List<CompetitionDto> competitions;
    private List<RecordDto> records;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CompetitionDto {
        private Integer id;
        private String name;
        private String date;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RecordDto {
        private Integer athleteId;
        private String athleteName;
        private String athleteAvatar;
        private String university;
        private String discipline;
        private String result;
        private String date;
    }
}