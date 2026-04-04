package com.ssau.kurs_back.dto.team;

import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class TeamMemberUpdateDto {

    @Min(value = 0, message = "Номер игрока не может быть отрицательным")
    private Integer playerNumber;

    private Integer playerPositionId;
}