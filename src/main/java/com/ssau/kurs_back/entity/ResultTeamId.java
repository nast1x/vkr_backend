package com.ssau.kurs_back.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResultTeamId implements Serializable {
    private Integer competitionDiscipline;
    private Integer team;
}