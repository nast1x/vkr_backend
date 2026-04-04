package com.ssau.kurs_back.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "\"Result_Personal\"", uniqueConstraints = {
        @UniqueConstraint(name = "\"AK_Result_Personal_Unique\"", columnNames = {"\"id_competition_discipline\"", "\"id_user\""})
})
@IdClass(ResultPersonalId.class)  // ✅ Добавлено: составной ключ
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResultPersonal {

    @Id  // ✅ Добавлено: часть первичного ключа
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "\"id_competition_discipline\"", referencedColumnName = "\"id_competition_discipline\"", nullable = false)
    @NotNull(message = "Дисциплина соревнования обязательна")
    private CompetitionDiscipline competitionDiscipline;

    @Id  // ✅ Добавлено: часть первичного ключа
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "\"id_user\"", referencedColumnName = "\"id_user\"", nullable = false)
    @NotNull(message = "Пользователь обязателен")
    private User user;

    @Min(value = 1, message = "Место должно быть больше 0")
    @Column(name = "\"rank_place\"")
    private Integer rankPlace;

    @Size(max = 100, message = "Значение результата слишком длинное")
    @Column(name = "\"result_value\"", length = 100)
    private String resultValue;
}