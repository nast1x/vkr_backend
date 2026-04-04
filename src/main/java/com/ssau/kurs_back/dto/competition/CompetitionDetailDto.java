package com.ssau.kurs_back.dto.competition;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDate;
import java.util.List;

@Data
@Builder
public class CompetitionDetailDto {
    private Integer idCompetition;
    private String name;
    private String competitionLevel;
    private String sportType;
    private String city;
    private LocalDate startDate;
    private LocalDate endDate;
    private VenueDto venue;
    private String organizer;
    private List<BestResultDto> bestResults;
    private List<ProtocolDto> protocols;

    @Data
    @Builder
    public static class VenueDto {
        private Integer id;
        private String name;
        private String address;
        private String photo;
    }

    @Data
    @Builder
    public static class BestResultDto {
        private Integer athleteId;
        private String athleteName;
        private String athleteAvatar;
        private String university;
        private String result;
        private String discipline;
    }

    @Data
    @Builder
    public static class ProtocolDto {
        private String discipline;
        private String type;
        private List<ProtocolResultDto> results;
    }

    @Data
    @Builder
    public static class ProtocolResultDto {
        private Integer athleteId;
        private String athleteName;
        private String university;
        private String result;
        private Integer rankPlace;
    }
}