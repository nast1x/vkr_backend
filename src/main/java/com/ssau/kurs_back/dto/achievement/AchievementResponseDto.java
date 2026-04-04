package com.ssau.kurs_back.dto.achievement;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class AchievementResponseDto {
    private Integer idAchievement;
    private Integer sportRankId;
    private String sportRankName;
    private Integer sportTypeId;
    private String sportTypeName;
    private Integer userId;
    private String userName;
    private LocalDate dateReceived;
    private Integer experienceYears;
}