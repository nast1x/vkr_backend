package com.ssau.kurs_back.entity.ref;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "\"Discipline_Stage\"", uniqueConstraints = {
        @UniqueConstraint(name = "\"AK_Discipline_Stage_Name\"", columnNames = {"\"stage_name\""}),
        @UniqueConstraint(name = "\"AK_Discipline_Stage_Number\"", columnNames = {"\"stage_number\""})
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DisciplineStage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "\"id_stage\"")
    private Integer idStage;

    @NotBlank(message = "Название этапа обязательно")
    @Size(max = 100, message = "Название этапа слишком длинное")
    @Column(name = "\"stage_name\"", nullable = false, unique = true, length = 100)
    private String stageName;

    @Size(max = 20, message = "Номер этапа слишком длинный")
    @Column(name = "\"stage_number\"", unique = true, length = 20)
    private String stageNumber; // "1/4", "1/8", "1/16"

    @Column(name = "\"stage_order\"", nullable = false)
    @Builder.Default
    private Integer stageOrder = 0;

    @Column(name = "\"description\"", columnDefinition = "TEXT")
    private String description;

    @PrePersist
    @PreUpdate
    private void trim() {
        if (stageName != null) stageName = stageName.trim();
        if (stageNumber != null) stageNumber = stageNumber.trim();
        if (description != null) description = description.trim();
    }
}