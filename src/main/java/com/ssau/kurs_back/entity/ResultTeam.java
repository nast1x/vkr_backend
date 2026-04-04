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
@Table(name = "\"Result_Team\"", uniqueConstraints = {
        @UniqueConstraint(name = "\"AK_Result_Team_Unique\"", columnNames = {"\"id_competition_discipline\"", "\"id_team\""})
})
@IdClass(ResultTeamId.class)  // ✅ Добавлено: составной ключ
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResultTeam {

    @Id  // ✅ Добавлено: часть первичного ключа
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "\"id_competition_discipline\"", referencedColumnName = "\"id_competition_discipline\"", nullable = false)
    @NotNull(message = "Дисциплина соревнования обязательна")
    private CompetitionDiscipline competitionDiscipline;

    @Id  // ✅ Добавлено: часть первичного ключа
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "\"id_team\"", referencedColumnName = "\"id_team\"", nullable = false)
    @NotNull(message = "Команда обязательна")
    private Team team;

    @Min(value = 1, message = "Место должно быть больше 0")
    @Column(name = "\"rank_place\"")
    private Integer rankPlace;

    @Size(max = 100, message = "Значение результата слишком длинное")
    @Column(name = "\"result_value\"", length = 100)
    private String resultValue;
}