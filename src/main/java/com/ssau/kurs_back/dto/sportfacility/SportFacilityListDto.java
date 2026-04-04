package com.ssau.kurs_back.dto.sportfacility;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({
        "idSportFacility",
        "name",
        "imageLink",
        "city",
        "address",
        "description",
        "sportTypes"
})
public class SportFacilityListDto {
    private Integer idSportFacility;
    private String name;
    private String imageLink;        // Фотография объекта
    private String description;       // Название (адрес: улица, дом)
    private String city;             // Город
    private String address;          // Полный адрес
    private String sportTypes;
}