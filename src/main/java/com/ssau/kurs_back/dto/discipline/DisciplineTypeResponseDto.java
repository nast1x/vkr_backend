package com.ssau.kurs_back.dto.discipline;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DisciplineTypeResponseDto {
    private Integer idDisciplineType;
    private Integer sportTypeId;
    private String sportTypeName;
    private String name;
    private String participantGender;
    private String participationType;
    private String description;
}
