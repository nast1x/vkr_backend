package com.ssau.kurs_back.entity;

import com.ssau.kurs_back.entity.ref.DisciplineStage;
import com.ssau.kurs_back.entity.ref.DisciplineType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "\"Competition_Discipline\"", uniqueConstraints = {
        @UniqueConstraint(name = "\"AK_Comp_Disc_Unique\"", columnNames = {
                "\"id_competition\"", "\"id_discipline_type\"", "\"id_stage\""
        })
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompetitionDiscipline {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "\"id_competition_discipline\"")
    private Integer idCompetitionDiscipline;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "\"id_discipline_type\"", referencedColumnName = "\"id_discipline_type\"", nullable = false)
    @NotNull(message = "Тип дисциплины обязателен")
    private DisciplineType disciplineType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "\"id_competition\"", referencedColumnName = "\"id_competition\"", nullable = false)
    @NotNull(message = "Соревнование обязательно")
    private Competition competition;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "\"id_stage\"", referencedColumnName = "\"id_stage\"", nullable = false)
    @NotNull(message = "Этап обязателен")
    private DisciplineStage stage;

    @NotBlank(message = "Название дисциплины обязательно")
    @Size(max = 255, message = "Название дисциплины слишком длинное")
    @Column(name = "\"discipline_name\"", nullable = false, length = 255)
    private String disciplineName;

    @OneToMany(mappedBy = "competitionDiscipline", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ResultPersonal> resultPersonalList;

    @OneToMany(mappedBy = "competitionDiscipline", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ResultTeam> resultTeamList;

    @PrePersist
    @PreUpdate
    private void trim() {
        if (disciplineName != null) disciplineName = disciplineName.trim();
    }
}