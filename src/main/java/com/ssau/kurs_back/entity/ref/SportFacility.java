package com.ssau.kurs_back.entity.ref;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "\"Sport_Facility\"", uniqueConstraints = {
        @UniqueConstraint(name = "AK_Facility_Address", columnNames = {"city", "street", "street_number"})
})
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SportFacility {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "\"id_sport_facility\"")
    private Integer idSportFacility;

    @NotBlank(message = "Название спортивного объекта обязательно")
    @Size(max = 255, message = "Название слишком длинное")
    @Column(name = "\"name\"", nullable = false, length = 255)
    private String name;

    @NotBlank(message = "Город обязателен для заполнения")
    @Size(max = 100, message = "Название города слишком длинное")
    @Column(name = "\"city\"", nullable = false, length = 100)
    private String city;

    @NotBlank(message = "Улица обязательна для заполнения")
    @Size(max = 255, message = "Название улицы слишком длинное")
    @Column(name = "\"street\"", nullable = false, length = 255)
    private String street;

    @NotBlank(message = "Номер дома обязателен")
    @Size(max = 20, message = "Номер дома слишком длинный")
    @Column(name = "\"street_number\"", nullable = false, length = 20)
    private String streetNumber;

    @Column(name = "\"description\"", columnDefinition = "TEXT")
    private String description;

    @Column(name = "\"image_link\"", columnDefinition = "TEXT")
    private String imageLink;

    @PrePersist
    @PreUpdate
    private void trim() {
        if (city != null) city = city.trim();
        if (street != null) street = street.trim();
        if (streetNumber != null) streetNumber = streetNumber.trim();
    }
}
