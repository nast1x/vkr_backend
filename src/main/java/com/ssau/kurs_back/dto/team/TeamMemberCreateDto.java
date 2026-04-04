package com.ssau.kurs_back.dto.team;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class TeamMemberCreateDto {

    @NotNull(message = "ID команды обязателен")
    private Integer teamId;

    @NotNull(message = "ID пользователя обязателен")
    private Integer userId;

    @Min(value = 0, message = "Номер игрока не может быть отрицательным")
    private Integer playerNumber;

    private Integer playerPositionId;
}