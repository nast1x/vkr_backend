package com.ssau.kurs_back.entity;

import com.ssau.kurs_back.entity.ref.SportType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "\"Team\"", uniqueConstraints = {
        @UniqueConstraint(name = "AK_Team_Name", columnNames = {"name"})
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_team")
    private Integer idTeam;

    @NotBlank(message = "Название команды обязательно")
    @Size(max = 255, message = "Название команды слишком длинное")
    @Column(name = "name", nullable = false, unique = true, length = 255)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_sport_type", referencedColumnName = "idSportType", nullable = false)
    @NotNull(message = "Вид спорта обязателен")
    private SportType sportType;

    @PrePersist
    @PreUpdate
    private void trim() {
        if (name != null) name = name.trim();
    }
}