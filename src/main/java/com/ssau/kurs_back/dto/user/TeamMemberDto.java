package com.ssau.kurs_back.dto.user;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonPropertyOrder({
        "userId",
        "fullName",
        "universityName",
        "universityCity",
        "roleName",
        "imageLink"
})
public class TeamMemberDto {
    private Integer userId;
    private String fullName;           // ФИО
    private String universityName;     // Название ВУЗа
    private String universityCity;     // Город обучения
    private String roleName;           // Спортсмен/Тренер
    private String imageLink;          // Ссылка на аватарку
}