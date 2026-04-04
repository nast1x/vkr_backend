package com.ssau.kurs_back.entity.ref;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "\"University\"")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class University {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idUniversity;

    @NotBlank(message = "Название вуза обязательно")
    @Size(max = 255, message = "Название слишком длинное")
    @Column(nullable = false, unique = true)
    private String name;

    @NotBlank(message = "Краткое название обязательно")
    @Size(max = 20, message = "Краткое название не может быть длиннее 20 символов")
    @Column(nullable = false, unique = true, length = 20)
    private String shortName;

    @NotBlank(message = "Город обязателен")
    @Size(max = 100, message = "Название города слишком длинное")
    @Column(nullable = false, length = 100)
    private String city;

    private String description;

    @PrePersist
    @PreUpdate
    private void trim() {
        if (name != null) name = name.trim();
        if (shortName != null) shortName = shortName.trim();
        if (city != null) city = city.trim();
    }
}
