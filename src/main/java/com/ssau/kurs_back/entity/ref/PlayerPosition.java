package com.ssau.kurs_back.entity.ref;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "\"Player_Position\"")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlayerPosition {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idPlayerPosition;

    @NotBlank(message = "Название позиции обязательно")
    @Size(max = 255, message = "Название слишком длинное")
    @Column(nullable = false, unique = true, length = 255)
    private String name;

    private String description;

    @PrePersist
    @PreUpdate
    private void trim() {
        if (name != null) name = name.trim();
    }
}
