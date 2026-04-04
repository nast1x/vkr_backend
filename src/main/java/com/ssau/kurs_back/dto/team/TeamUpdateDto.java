package com.ssau.kurs_back.dto.team;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class TeamUpdateDto {

    @Size(max = 255, message = "Название команды слишком длинное")
    private String name;

    private Integer sportTypeId;
}