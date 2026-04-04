package com.ssau.kurs_back.dto.user;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class UserResponseDto {
    private Integer idUser;
    private String email;
    private String lastName;
    private String firstName;
    private String middleName;
    private LocalDate birthDate;
    private String gender;
    private String imageLink;
    private Integer roleId;
    private String roleName;
    private Integer coachId;
    private String coachName;
}