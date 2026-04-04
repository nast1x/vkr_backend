package com.ssau.kurs_back.dto.discipline;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class DisciplineTypeCreateDto {

    @NotNull(message = "ID вида спорта обязателен")
    private Integer sportTypeId;

    // ✅ НОВОЕ ПОЛЕ
    @NotBlank(message = "Название типа дисциплины обязательно")
    @Size(max = 255, message = "Название слишком длинное")
    private String name;

    @NotBlank(message = "Пол участников обязателен")
    @Size(max = 10, message = "Значение пола слишком длинное")
    private String participantGender;

    @NotBlank(message = "Тип участия обязателен")
    @Size(max = 50, message = "Тип участия слишком длинный")
    private String participationType;

    private String description;
}