package com.ssau.kurs_back.repository.ref;

import com.ssau.kurs_back.dto.university.UniversityStatsDto;
import com.ssau.kurs_back.entity.ref.University;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UniversityRepository extends JpaRepository<University, Integer> {

    @Query(value = """
                SELECT 
                    u.id_university,
                    u.image_link,
                    u.short_name,
                    u.city,
                    COALESCE(SUM(CASE WHEN r.name = 'Athlete' THEN 1 ELSE 0 END), 0)::int AS athletes_count,
                    COALESCE(SUM(CASE WHEN r.name = 'Coach' THEN 1 ELSE 0 END), 0)::int AS coaches_count
                FROM "University" u
                LEFT JOIN "Education_Place" ep ON u.id_university = ep.id_university
                LEFT JOIN "Users" usr ON ep.id_user = usr.id_user
                LEFT JOIN "Role" r ON usr.id_role = r.id_role
                GROUP BY u.id_university, u.short_name, u.city
                ORDER BY u.id_university
            """, nativeQuery = true)
    List<UniversityStatsDto> getUniversityStats();

    List<University> findByNameContainingIgnoreCase(String name);

    @Query(value = """
                SELECT 
                    u.id_university,
                    u.image_link,
                    u.name,
                    u.short_name,
                    u.city,
                    u.description,
                    COALESCE((SELECT COUNT(*) FROM "Education_Place" ep 
                              JOIN "Users" usr ON ep.id_user = usr.id_user 
                              JOIN "Role" r ON usr.id_role = r.id_role 
                              WHERE ep.id_university = u.id_university AND r.name = 'Athlete'), 0) AS total_athletes,
                    COALESCE((SELECT COUNT(*) FROM "Education_Place" ep 
                              JOIN "Users" usr ON ep.id_user = usr.id_user 
                              JOIN "Role" r ON usr.id_role = r.id_role 
                              WHERE ep.id_university = u.id_university AND r.name = 'Coach'), 0) AS total_coaches,
                    COALESCE((SELECT COUNT(*) + 1 FROM (
                        SELECT u2.id_university
                        FROM "University" u2
                        LEFT JOIN "Education_Place" ep2 ON u2.id_university = ep2.id_university
                        LEFT JOIN "Users" usr2 ON ep2.id_user = usr2.id_user
                        LEFT JOIN "Result_Personal" rp2 ON usr2.id_user = rp2.id_user
                        GROUP BY u2.id_university
                        HAVING COALESCE(SUM(CASE WHEN rp2.rank_place <= 3 THEN 1 ELSE 0 END), 0) > 
                            (SELECT COALESCE(SUM(CASE WHEN rp.rank_place <= 3 THEN 1 ELSE 0 END), 0)
                             FROM "Education_Place" ep
                             JOIN "Users" usr ON ep.id_user = usr.id_user
                             JOIN "Result_Personal" rp ON usr.id_user = rp.id_user
                             WHERE ep.id_university = u.id_university)
                    ) subquery), 999) AS rating_place
                FROM "University" u
                WHERE u.id_university = :universityId
            """, nativeQuery = true)
    Optional<Object> findUniversityDetail(@Param("universityId") Integer universityId);

    @Query(value = """
                SELECT 
                    coach.id_user,
                    coach.last_name || ' ' || coach.first_name || ' ' || COALESCE(coach.middle_name, '') AS full_name,
                    coach.image_link,
                    COALESCE(st.name, 'Без специализации') AS specialization,
                    athlete.id_user AS athlete_id,
                    athlete.last_name || ' ' || athlete.first_name || ' ' || COALESCE(athlete.middle_name, '') AS athlete_name,
                    athlete.image_link AS athlete_avatar,
                    COALESCE(st2.name, 'Без вида спорта') AS athlete_sport
                FROM "Education_Place" ep
                JOIN "Users" coach ON ep.id_user = coach.id_user
                JOIN "Role" r ON coach.id_role = r.id_role
                LEFT JOIN "Users" athlete ON athlete.id_coach = coach.id_user
                LEFT JOIN "Education_Place" ep2 ON athlete.id_user = ep2.id_user
                LEFT JOIN "Achievement" ach ON athlete.id_user = ach.id_user
                LEFT JOIN "Sport_Type" st2 ON ach.id_sport_type = st2.id_sport_type
                LEFT JOIN "Sport_Type" st ON st.id_sport_type = ach.id_sport_type
                WHERE ep.id_university = :universityId
                    AND r.name = 'Coach'
                ORDER BY coach.id_user, athlete.id_user
            """, nativeQuery = true)
    List<Object[]> findCoachesWithAthletes(Integer universityId);
}