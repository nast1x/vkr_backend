package com.ssau.kurs_back.dto.team;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class TeamCreateDto {

    @NotBlank(message = "Название команды обязательно")
    @Size(max = 255, message = "Название команды слишком длинное")
    private String name;

    @NotNull(message = "ID вида спорта обязателен")
    private Integer sportTypeId;
}