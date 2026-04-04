package com.ssau.kurs_back.service;

import com.ssau.kurs_back.dto.statistics.StatisticsDto;
import com.ssau.kurs_back.dto.statistics.StatisticsDto.*;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StatisticsService {

    private final EntityManager entityManager;

    public StatisticsDto getStatistics() {
        return StatisticsDto.builder()
                .generalStats(getGeneralStats())
                .topUniversities(getUniversityRanking())
                .topAthletes(getTopAthletes())
                .sportsStatistics(getSportStatistics())
                .cityStatistics(getCityStatistics())
                .build();
    }

    private GeneralStats getGeneralStats() {
        String sql = """
                    SELECT 
                        (SELECT COUNT(*) FROM "Users" u JOIN "Role" r ON u.id_role = r.id_role WHERE r.name = 'Athlete') AS athletes,
                        (SELECT COUNT(*) FROM "Users" u JOIN "Role" r ON u.id_role = r.id_role WHERE r.name = 'Coach') AS coaches,
                        (SELECT COUNT(*) FROM "University") AS universities,
                        (SELECT COUNT(*) FROM "Competition") AS competitions
                """;

        Object[] result = (Object[]) entityManager.createNativeQuery(sql).getSingleResult();

        return GeneralStats.builder()
                .totalAthletes(convertToInt(result[0]))
                .totalCoaches(convertToInt(result[1]))
                .totalUniversities(convertToInt(result[2]))
                .totalCompetitions(convertToInt(result[3]))
                .build();
    }

    private List<UniversityRankingDto> getUniversityRanking() {
        String sql = """
                    SELECT 
                        u.id_university,
                        u.name,
                        u.city,
                        COALESCE(SUM(CASE WHEN rp.rank_place = 1 THEN 1 ELSE 0 END), 0) AS gold,
                        COALESCE(SUM(CASE WHEN rp.rank_place = 2 THEN 1 ELSE 0 END), 0) AS silver,
                        COALESCE(SUM(CASE WHEN rp.rank_place = 3 THEN 1 ELSE 0 END), 0) AS bronze,
                        COALESCE(SUM(CASE WHEN rp.rank_place <= 3 THEN 1 ELSE 0 END), 0) AS total
                    FROM "University" u
                    JOIN "Education_Place" ep ON u.id_university = ep.id_university
                    JOIN "Users" usr ON ep.id_user = usr.id_user
                    JOIN "Result_Personal" rp ON usr.id_user = rp.id_user
                    GROUP BY u.id_university, u.name, u.city
                    ORDER BY total DESC, gold DESC, silver DESC, bronze DESC
                """;

        List<Object[]> results = entityManager.createNativeQuery(sql).getResultList();
        return mapUniversityRanking(results);
    }

    private List<AthleteRankingDto> getTopAthletes() {
        String sql = """
                    SELECT 
                        usr.id_user,
                        usr.last_name || ' ' || usr.first_name || ' ' || COALESCE(usr.middle_name, '') AS name,
                        usr.image_link,
                        u.name AS university,
                        dt.name AS sport,
                        COUNT(rp.id_competition_discipline) AS medals,
                        COUNT(DISTINCT cd.id_competition) AS competitions
                    FROM "Users" usr
                    JOIN "Education_Place" ep ON usr.id_user = ep.id_user
                    JOIN "University" u ON ep.id_university = u.id_university
                    JOIN "Result_Personal" rp ON usr.id_user = rp.id_user
                    JOIN "Competition_Discipline" cd ON rp.id_competition_discipline = cd.id_competition_discipline
                    JOIN "Discipline_Type" dt ON cd.id_discipline_type = dt.id_discipline_type
                    JOIN "Role" r ON usr.id_role = r.id_role
                    WHERE r.name = 'Athlete'
                    GROUP BY usr.id_user, usr.last_name, usr.first_name, usr.middle_name, 
                             usr.image_link, u.name, dt.name
                    ORDER BY medals DESC, competitions DESC
                """;

        List<Object[]> results = entityManager.createNativeQuery(sql).getResultList();
        return mapAthleteRanking(results);
    }

    private List<SportStatisticsDto> getSportStatistics() {
        String sql = """
                    SELECT 
                        st.name,
                        COUNT(DISTINCT usr.id_user) AS athletes,
                        COUNT(DISTINCT c.id_competition) AS competitions,
                        COUNT(DISTINCT sf.id_sport_facility) AS facilities,
                        ROUND(COUNT(DISTINCT usr.id_user) * 100.0 / 
                            NULLIF((SELECT COUNT(*) FROM "Users" WHERE id_role = 
                                (SELECT id_role FROM "Role" WHERE name = 'Athlete')), 0), 2) AS percentage
                    FROM "Sport_Type" st
                    LEFT JOIN "Competition" c ON st.id_sport_type = c.id_sport_type
                    LEFT JOIN "Competition_Discipline" cd ON c.id_competition = cd.id_competition
                    LEFT JOIN "Discipline_Type" dt ON cd.id_discipline_type = dt.id_discipline_type
                    LEFT JOIN "Result_Personal" rp ON cd.id_competition_discipline = rp.id_competition_discipline
                    LEFT JOIN "Users" usr ON rp.id_user = usr.id_user
                    LEFT JOIN "Sport_Facility" sf ON c.id_sport_facility = sf.id_sport_facility
                    JOIN "Role" r ON usr.id_role = r.id_role
                    WHERE r.name = 'Athlete'  
                    GROUP BY st.id_sport_type, st.name
                    ORDER BY athletes DESC
                """;

        List<Object[]> results = entityManager.createNativeQuery(sql).getResultList();
        return mapSportStatistics(results);
    }

    private List<CityStatisticsDto> getCityStatistics() {
        String sql = """
                    SELECT 
                        u.city,
                        COUNT(DISTINCT usr.id_user) AS athletes,
                        COUNT(DISTINCT c.id_competition) AS competitions,
                        COUNT(DISTINCT usr.id_user) + COUNT(DISTINCT c.id_competition) AS total
                    FROM "University" u
                    JOIN "Education_Place" ep ON u.id_university = ep.id_university
                    JOIN "Users" usr ON ep.id_user = usr.id_user
                    LEFT JOIN "Result_Personal" rp ON usr.id_user = rp.id_user
                    LEFT JOIN "Competition_Discipline" cd ON rp.id_competition_discipline = cd.id_competition_discipline
                    LEFT JOIN "Competition" c ON cd.id_competition = c.id_competition
                    JOIN "Role" r ON usr.id_role = r.id_role
                    WHERE r.name = 'Athlete'
                    GROUP BY u.city
                    ORDER BY total DESC
                """;

        List<Object[]> results = entityManager.createNativeQuery(sql).getResultList();
        return mapCityStatistics(results);
    }

    // ===== Маппинг результатов =====

    private List<UniversityRankingDto> mapUniversityRanking(List<Object[]> results) {
        List<UniversityRankingDto> ranking = new ArrayList<>();
        for (Object[] row : results) {
            ranking.add(UniversityRankingDto.builder()
                    .id(convertToInt(row[0]))
                    .name((String) row[1])
                    .city((String) row[2])
                    .gold(convertToInt(row[3]))
                    .silver(convertToInt(row[4]))
                    .bronze(convertToInt(row[5]))
                    .total(convertToInt(row[6]))
                    .build());
        }
        return ranking;
    }

    private List<AthleteRankingDto> mapAthleteRanking(List<Object[]> results) {
        List<AthleteRankingDto> athletes = new ArrayList<>();
        for (Object[] row : results) {
            athletes.add(AthleteRankingDto.builder()
                    .id(convertToInt(row[0]))
                    .name((String) row[1])
                    .avatar(row[2] != null ? (String) row[2] : "/assets/images/avatar-placeholder.png")
                    .university((String) row[3])
                    .sport((String) row[4])
                    .medals(convertToInt(row[5]))
                    .competitions(convertToInt(row[6]))
                    .build());
        }
        return athletes;
    }

    private List<SportStatisticsDto> mapSportStatistics(List<Object[]> results) {
        List<SportStatisticsDto> stats = new ArrayList<>();
        for (Object[] row : results) {
            stats.add(SportStatisticsDto.builder()
                    .name((String) row[0])
                    .athletes(convertToInt(row[1]))
                    .competitions(convertToInt(row[2]))
                    .facilities(convertToInt(row[3]))
                    .percentage(row[4] != null ? ((Number) row[4]).doubleValue() : 0.0)
                    .build());
        }
        return stats;
    }

    private List<CityStatisticsDto> mapCityStatistics(List<Object[]> results) {
        List<CityStatisticsDto> stats = new ArrayList<>();
        for (Object[] row : results) {
            stats.add(CityStatisticsDto.builder()
                    .name((String) row[0])
                    .athletes(convertToInt(row[1]))
                    .competitions(convertToInt(row[2]))
                    .total(convertToInt(row[3]))
                    .build());
        }
        return stats;
    }

    private Integer convertToInt(Object value) {
        if (value == null) return 0;
        if (value instanceof Number) return ((Number) value).intValue();
        return Integer.parseInt(value.toString());
    }
}