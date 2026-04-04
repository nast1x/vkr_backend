package com.ssau.kurs_back.entity;

import com.ssau.kurs_back.entity.ref.Major;
import com.ssau.kurs_back.entity.ref.University;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "\"Education_Place\"", uniqueConstraints = {
        @UniqueConstraint(name = "AK_User_Education", columnNames = {"id_user", "id_university"})
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EducationPlace {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_education_place")
    private Integer idEducationPlace;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_user", referencedColumnName = "id_user", nullable = false)
    @NotNull(message = "Пользователь обязателен")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_university", referencedColumnName = "idUniversity", nullable = false)
    @NotNull(message = "Вуз обязателен")
    private University university;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_major", referencedColumnName = "idMajor", nullable = false)
    @NotNull(message = "Специальность обязательна")
    private Major major;

    @Min(value = 0, message = "Курс должен быть от 0 до 6")
    @Max(value = 6, message = "Курс должен быть от 0 до 6")
    @Column(name = "course_year")
    private Integer courseYear;
}