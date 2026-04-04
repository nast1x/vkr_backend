package com.ssau.kurs_back.dto.education;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class EducationPlaceCreateDto {

    @NotNull(message = "ID пользователя обязателен")
    private Integer userId;

    @NotNull(message = "ID вуза обязателен")
    private Integer universityId;

    @NotNull(message = "ID специальности обязателен")
    private Integer majorId;

    @Min(value = 0, message = "Курс должен быть от 0 до 6")
    @Max(value = 6, message = "Курс должен быть от 0 до 6")
    private Integer courseYear;
}