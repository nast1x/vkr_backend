package com.ssau.kurs_back.entity.ref;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "\"Sport_Type\"")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SportType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idSportType;

    @NotBlank(message = "Название вида спорта обязательно")
    @Size(max = 100, message = "Название слишком длинное")
    @Column(nullable = false, unique = true, length = 100)
    private String name;

    private String description;

    @PrePersist
    @PreUpdate
    private void trim() {
        if (name != null) name = name.trim();
    }
}
