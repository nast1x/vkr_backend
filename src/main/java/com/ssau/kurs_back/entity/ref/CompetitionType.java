package com.ssau.kurs_back.entity.ref;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "\"Competition_Type\"")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompetitionType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idCompetitionType;

    @NotBlank(message = "Название типа соревнования обязательно")
    @Size(max = 255, message = "Название не может превышать 255 символов")
    @Column(nullable = false, unique = true, length = 255)
    private String name;

    private String description;

    @PrePersist
    @PreUpdate
    private void trim() {
        if (name != null) name = name.trim();
    }
}
