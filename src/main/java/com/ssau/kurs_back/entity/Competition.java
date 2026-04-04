package com.ssau.kurs_back.entity;

import com.ssau.kurs_back.entity.ref.CompetitionType;
import com.ssau.kurs_back.entity.ref.SportFacility;
import com.ssau.kurs_back.entity.ref.SportType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "\"Competition\"")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Competition {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "\"id_competition\"")
    private Integer idCompetition;

    @NotBlank(message = "Название соревнования обязательно")
    @Size(max = 255, message = "Название слишком длинное")
    @Column(name = "\"name\"", nullable = false, length = 255)
    private String name;

    @NotNull(message = "Дата начала обязательна")
    @Column(name = "\"start_date\"", nullable = false)
    private LocalDate startDate;

    @Column(name = "\"end_date\"")
    private LocalDate endDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "\"id_competition_type\"", referencedColumnName = "idCompetitionType", nullable = false)
    @NotNull(message = "Тип соревнования обязателен")
    private CompetitionType competitionType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "\"id_sport_facility\"", referencedColumnName = "\"id_sport_facility\"", nullable = false)
    @NotNull(message = "Спортивный объект обязателен")
    private SportFacility sportFacility;

    @NotBlank(message = "Организатор обязателен")
    @Size(max = 255, message = "Название организатора слишком длинное")
    @Column(name = "\"organizer\"", length = 255)
    private String organizer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "\"id_sport_type\"", referencedColumnName = "idSportType", nullable = false)
    @NotNull(message = "Вид спорта обязателен")
    private SportType sportType;

    @Column(name = "\"description\"", columnDefinition = "TEXT")
    private String description;

    @PrePersist
    @PreUpdate
    private void trim() {
        if (organizer != null) organizer = organizer.trim();
        if (description != null) description = description.trim();
    }
}