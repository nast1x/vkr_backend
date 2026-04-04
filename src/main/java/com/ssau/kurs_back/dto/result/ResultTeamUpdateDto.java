package com.ssau.kurs_back.dto.result;

import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class ResultTeamUpdateDto {

    @Min(value = 1, message = "Место должно быть больше 0")
    private Integer rankPlace;

    private String resultValue;
}