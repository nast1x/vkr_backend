package com.ssau.kurs_back.entity.ref;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "\"Major\"")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Major {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idMajor;

    @NotBlank(message = "Название специальности обязательно")
    @Size(max = 255, message = "Название слишком длинное")
    @Column(nullable = false, unique = true)
    private String name;

    @NotBlank(message = "Код специальности обязателен")
    @Pattern(regexp = "^\\d{2}\\.\\d{2}\\.\\d{2}$", message = "Формат кода должен быть XX.XX.XX (например, 09.03.01)")
    @Column(nullable = false, unique = true, length = 10)
    private String code;

    private String description;

    @PrePersist
    @PreUpdate
    private void trim() {
        if (name != null) name = name.trim();
        if (code != null) code = code.trim();
    }
}
