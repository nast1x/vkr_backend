package com.ssau.kurs_back.dto.education;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EducationPlaceResponseDto {
    private Integer idEducationPlace;
    private Integer userId;
    private String userName;
    private Integer universityId;
    private String universityName;
    private String universityShortName;
    private Integer majorId;
    private String majorName;
    private String majorCode;
    private Integer courseYear;
}