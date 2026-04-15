package com.ssau.kurs_back.dto.achievement;

import lombok.Data;
import java.time.LocalDate;

@Data
public class SportRankAssignmentDto {
    private Integer userId;
    private Integer sportTypeId;
    private Integer rankId;
    private LocalDate dateReceived;
}
