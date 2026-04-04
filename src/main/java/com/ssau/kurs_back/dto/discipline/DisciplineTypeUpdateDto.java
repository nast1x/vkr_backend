package com.ssau.kurs_back.dto.discipline;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class DisciplineTypeUpdateDto {

    private Integer sportTypeId;

    // ✅ НОВОЕ ПОЛЕ
    @Size(max = 255, message = "Название слишком длинное")
    private String name;

    @Size(max = 10, message = "Значение пола слишком длинное")
    private String participantGender;

    @Size(max = 50, message = "Тип участия слишком длинный")
    private String participationType;

    private String description;
}