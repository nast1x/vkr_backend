package com.ssau.kurs_back.entity;

import com.ssau.kurs_back.entity.ref.PlayerPosition;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "\"Team_Member\"")
@IdClass(TeamMemberId.class)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TeamMember {

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_team", referencedColumnName = "id_team", nullable = false)
    @NotNull(message = "Команда обязательна")
    private Team team;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_user", referencedColumnName = "id_user", nullable = false)
    @NotNull(message = "Пользователь обязателен")
    private User user;

    @Min(value = 0, message = "Номер игрока не может быть отрицательным")
    @Column(name = "player_number")
    private Integer playerNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_player_position", referencedColumnName = "idPlayerPosition")
    private PlayerPosition playerPosition;
}