package com.ssau.kurs_back.repository;

import com.ssau.kurs_back.dto.user.TeamMemberDto;
import com.ssau.kurs_back.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    @Query("""
                SELECT new com.ssau.kurs_back.dto.user.TeamMemberDto(
                    u.idUser,
                    CONCAT(u.lastName, ' ', u.firstName, ' ', COALESCE(u.middleName, '')),
                    ep.university.shortName,
                    ep.university.city,
                    r.name, 
                    u.imageLink                                
                )
                FROM User u
                JOIN EducationPlace ep ON u.idUser = ep.user.idUser
                JOIN Role r ON u.role.idRole = r.idRole
                WHERE r.name IN ('Athlete', 'Coach')
                ORDER BY u.idUser
            """)
    List<TeamMemberDto> findTeamMembers();

    Optional<User> findByEmail(String email);

    List<User> findByCoachIdUser(Integer coachId);

    List<User> findByLastNameContainingIgnoreCase(String lastName);

    boolean existsByEmail(String email);

    @Query(value = """
    SELECT 
        u.id_university,
        u.name,
        m.name,
        ep.course_year
    FROM "Education_Place" ep
    JOIN "University" u ON ep.id_university = u.id_university
    JOIN "Major" m ON ep.id_major = m.id_major
    WHERE ep.id_user = :userId
    LIMIT 1
""", nativeQuery = true)
    List<Object[]> findEducationPlace(@Param("userId") Integer userId);

    @Query(value = """
    SELECT 
        st.name,
        sr.name,
        a.date_received
    FROM "Achievement" a
    JOIN "Sport_Type" st ON a.id_sport_type = st.id_sport_type
    JOIN "Sport_Rank" sr ON a.id_sport_rank = sr.id_sport_rank
    WHERE a.id_user = :userId
    ORDER BY a.date_received DESC
""", nativeQuery = true)
    List<Object[]> findSportInfo(@Param("userId") Integer userId);

    @Query(value = """
        SELECT 
            u.id_user,
            u.last_name || ' ' || u.first_name || ' ' || COALESCE(u.middle_name, '') AS full_name,
            u.image_link
        FROM "Users" u
        WHERE u.id_coach = :coachId
    """, nativeQuery = true)
    List<Object[]> findTraineesByCoachId(@Param("coachId") Integer coachId);

    @Query(value = """
        SELECT 
            cd.discipline_name,
            rp.result_value,
            TO_CHAR(c.start_date, 'DD.MM.YYYY') AS competition_date,
            c.id_competition,
            c.name AS competition_name
        FROM "Result_Personal" rp
        JOIN "Competition_Discipline" cd ON rp.id_competition_discipline = cd.id_competition_discipline
        JOIN "Competition" c ON cd.id_competition = c.id_competition
        WHERE rp.id_user = :userId
        ORDER BY c.start_date DESC
    """, nativeQuery = true)
    List<Object[]> findPersonalRecords(@Param("userId") Integer userId);

    @Query("""
        SELECT u FROM User u
        LEFT JOIN EducationPlace ep ON u.idUser = ep.user.idUser
        WHERE ep.idEducationPlace IS NULL
    """)
    List<User> findUsersWithoutEducationPlace();

}
