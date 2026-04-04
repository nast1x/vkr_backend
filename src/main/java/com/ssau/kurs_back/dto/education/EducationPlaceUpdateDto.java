package com.ssau.kurs_back.dto.education;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class EducationPlaceUpdateDto {

    private Integer universityId;

    private Integer majorId;

    @Min(value = 0, message = "Курс должен быть от 0 до 6")
    @Max(value = 6, message = "Курс должен быть от 0 до 6")
    private Integer courseYear;
}