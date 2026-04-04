package com.ssau.kurs_back.entity;

import com.ssau.kurs_back.entity.ref.SportRank;
import com.ssau.kurs_back.entity.ref.SportType;
import jakarta.persistence.*;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "\"Achievement\"")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Achievement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_achievement")
    private Integer idAchievement;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_sport_rank", referencedColumnName = "idSportRank", nullable = false)
    @NotNull(message = "Спортивный разряд обязателен")
    private SportRank sportRank;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_sport_type", referencedColumnName = "idSportType", nullable = false)
    @NotNull(message = "Вид спорта обязателен")
    private SportType sportType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_user", referencedColumnName = "id_user", nullable = false)
    @NotNull(message = "Пользователь обязателен")
    private User user;

    @NotNull(message = "Дата получения обязательна")
    @Column(name = "date_received", nullable = false)
    private LocalDate dateReceived;

    @Min(value = 0, message = "Стаж не может быть отрицательным")
    @Column(name = "experience_years")
    private Integer experienceYears;
}