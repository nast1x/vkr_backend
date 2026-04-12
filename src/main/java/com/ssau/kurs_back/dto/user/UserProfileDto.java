package com.ssau.kurs_back.dto.user;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDate;
import java.util.List;

@Data
@Builder
public class UserProfileDto {
    private Integer id;
    private String fullName;
    private String email;
    private String avatar;
    private String role;
    private Integer age;
    private LocalDate birthDate;
    private String gender;

    // Место обучения
    private Integer universityId;
    private String university;
    private String faculty;
    private Integer course;

    // Тренер (если есть)
    private Integer coachId;
    private String coachName;

    // Вид спорта
    private List<UserSportDto> sport;
    private List<UserSportDto> category;
    private List<UserSportDto> dateReceived;

    // Воспитанники (если тренер)
    private List<TraineeDto> trainees;

    // Результаты (если спортсмен)
    private List<PersonalRecordDto> records;

    @Data
    @Builder
    public static class TraineeDto {
        private Integer id;
        private String fullName;
        private String avatar;
        private String sport;
    }

    @Data
    @Builder
    public static class PersonalRecordDto {
        private String discipline;
        private String result;
        private String date;
        private Integer competitionId;
        private String competitionName;
    }
    @Data
    @Builder
    public static class UserSportDto {
        private String sportName;
        private String rankName;
        private LocalDate dateReceived;
    }
}