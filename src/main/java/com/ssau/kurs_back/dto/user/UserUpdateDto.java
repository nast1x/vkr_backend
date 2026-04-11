package com.ssau.kurs_back.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UserUpdateDto {

    @Email(message = "Некорректный формат email")
    private String email;

    @Size(min = 8, message = "Пароль должен быть минимум 8 символов")
    private String password;

    private String oldPassword;

    private String lastName;

    private String firstName;

    private String middleName;

    private LocalDate birthDate;

    private String gender;

    private String imageLink;

    private Integer roleId;

    private Integer coachId;
}