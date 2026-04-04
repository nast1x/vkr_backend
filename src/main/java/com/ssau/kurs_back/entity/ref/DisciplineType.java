package com.ssau.kurs_back.entity.ref;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "\"Discipline_Type\"", uniqueConstraints = {
        @UniqueConstraint(name = "\"AK_Discipline_Type_Full\"", columnNames = {
                "\"id_sport_type\"", "\"participant_gender\"", "\"participation_type\""
        }),
        @UniqueConstraint(name = "\"AK_Discipline_Type_Name\"", columnNames = {
                "\"id_sport_type\"", "\"name\""
        })
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DisciplineType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "\"id_discipline_type\"")
    private Integer idDisciplineType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "\"id_sport_type\"", referencedColumnName = "idSportType", nullable = false)
    @NotNull(message = "Вид спорта обязателен")
    private SportType sportType;

    @NotBlank(message = "Название типа дисциплины обязательно")
    @Size(max = 255, message = "Название слишком длинное")
    @Column(name = "\"name\"", nullable = false, length = 255)
    private String name;

    @Column(name = "\"description\"", columnDefinition = "TEXT")
    private String description;

    @NotBlank(message = "Пол участников обязателен")
    @Size(max = 10, message = "Значение пола слишком длинное")
    @Column(name = "\"participant_gender\"", nullable = false, length = 10)
    private String participantGender; // "Male", "Female", "Mixed"

    @NotBlank(message = "Тип участия обязателен")
    @Size(max = 50, message = "Тип участия слишком длинный")
    @Column(name = "\"participation_type\"", nullable = false, length = 50)
    private String participationType; // "Personal", "Team"

    @PrePersist
    @PreUpdate
    private void trim() {
        if (name != null) name = name.trim();
        if (description != null) description = description.trim();
        if (participantGender != null) participantGender = participantGender.trim();
        if (participationType != null) participationType = participationType.trim();
    }
}