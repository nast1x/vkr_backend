package com.ssau.kurs_back.entity;

import com.ssau.kurs_back.entity.ref.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "\"Users\"", uniqueConstraints = {
        @UniqueConstraint(name = "AK_User_Email", columnNames = {"email"})
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_user")
    private Integer idUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_coach", referencedColumnName = "id_user")
    private User coach;

    @NotBlank(message = "Email обязателен")
    @Email(message = "Некорректный формат email")
    @Size(max = 255, message = "Email слишком длинный")
    @Column(name = "email", nullable = false, unique = true, length = 255)
    private String email;

    @NotBlank(message = "Пароль обязателен")
    @Size(min = 8, max = 255, message = "Пароль должен быть от 8 до 255 символов")
    @Column(name = "password", nullable = false, length = 255)
    private String password;

    @NotBlank(message = "Фамилия обязательна")
    @Size(max = 100, message = "Фамилия слишком длинная")
    @Column(name = "last_name", nullable = false, length = 100)
    private String lastName;

    @NotBlank(message = "Имя обязательно")
    @Size(max = 100, message = "Имя слишком длинное")
    @Column(name = "first_name", nullable = false, length = 100)
    private String firstName;

    @Size(max = 100, message = "Отчество слишком длинное")
    @Column(name = "middle_name", length = 100)
    private String middleName;

    @Column(name = "birth_date")
    private LocalDate birthDate;

    @Size(max = 10, message = "Значение пола слишком длинное")
    @Column(name = "gender", length = 10)
    private String gender;

    @Column(name = "image_link", columnDefinition = "TEXT")
    private String imageLink;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_role", nullable = false)
    @NotNull(message = "Роль обязательна")
    private Role role;

    @PrePersist
    @PreUpdate
    private void trim() {
        if (email != null) email = email.trim();
        if (lastName != null) lastName = lastName.trim();
        if (firstName != null) firstName = firstName.trim();
        if (middleName != null) middleName = middleName.trim();
        if (gender != null) gender = gender.trim();
    }
}