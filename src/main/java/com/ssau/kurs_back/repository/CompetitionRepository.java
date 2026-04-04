package com.ssau.kurs_back.repository;

import com.ssau.kurs_back.dto.competition.CompetitionListDto;
import com.ssau.kurs_back.entity.Competition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface CompetitionRepository extends JpaRepository<Competition, Integer> {
    List<Competition> findBySportTypeIdSportType(Integer sportTypeId);
    List<Competition> findByCompetitionTypeIdCompetitionType(Integer competitionTypeId);
    List<Competition> findBySportFacilityIdSportFacility(Integer sportFacilityId);
    List<Competition> findByStartDateBetween(LocalDate startDate, LocalDate endDate);
    List<Competition> findByStartDateAfter(LocalDate date);
    List<Competition> findByOrganizerContainingIgnoreCase(String organizer);

    @Query("""
        SELECT new com.ssau.kurs_back.dto.competition.CompetitionListDto(
            c.idCompetition,
            c.name,
            ct.name,
            st.name,
            sf.city,
            c.startDate,
            c.endDate
        )
        FROM Competition c
        JOIN c.competitionType ct
        JOIN c.sportType st
        JOIN c.sportFacility sf
        ORDER BY c.startDate ASC
    """)
    List<CompetitionListDto> findCompetitionList();

    @Query(value = """
        SELECT 
            c.id_competition,
            c.name,
            ct.name AS competition_level,
            st.name AS sport_type,
            sf.city,
            c.start_date,
            c.end_date,
            sf.id_sport_facility AS venue_id,
            sf.name AS venue_name,
            sf.city || ', ' || sf.street || ', д. ' || sf.street_number AS venue_address,
            sf.image_link AS venue_photo,
            c.organizer
        FROM "Competition" c
        JOIN "Competition_Type" ct ON c.id_competition_type = ct.id_competition_type
        JOIN "Sport_Type" st ON c.id_sport_type = st.id_sport_type
        JOIN "Sport_Facility" sf ON c.id_sport_facility = sf.id_sport_facility
        WHERE c.id_competition = :competitionId
    """, nativeQuery = true)
    Optional<Object> findCompetitionDetail(@Param("competitionId") Integer competitionId);

    @Query(value = """
        SELECT 
            usr.id_user,
            usr.last_name || ' ' || usr.first_name || ' ' || COALESCE(usr.middle_name, '') AS athlete_name,
            usr.image_link,
            u.short_name AS university,
            rp.result_value,
            cd.discipline_name
        FROM "Result_Personal" rp
        JOIN "Users" usr ON rp.id_user = usr.id_user
        JOIN "Education_Place" ep ON usr.id_user = ep.id_user
        JOIN "University" u ON ep.id_university = u.id_university
        JOIN "Competition_Discipline" cd ON rp.id_competition_discipline = cd.id_competition_discipline
        WHERE cd.id_competition = :competitionId
            AND rp.rank_place = 1
        ORDER BY cd.discipline_name ASC
    """, nativeQuery = true)
    List<Object[]> findBestResults(@Param("competitionId") Integer competitionId);

    @Query(value = """
        SELECT 
            cd.discipline_name,
            dt.participant_gender,
            usr.id_user,
            usr.last_name || ' ' || usr.first_name || ' ' || COALESCE(usr.middle_name, '') AS athlete_name,
            u.short_name AS university,
            rp.result_value,
            rp.rank_place
        FROM "Competition_Discipline" cd
        JOIN "Discipline_Type" dt ON cd.id_discipline_type = dt.id_discipline_type
        LEFT JOIN "Result_Personal" rp ON cd.id_competition_discipline = rp.id_competition_discipline
        LEFT JOIN "Users" usr ON rp.id_user = usr.id_user
        LEFT JOIN "Education_Place" ep ON usr.id_user = ep.id_user
        LEFT JOIN "University" u ON ep.id_university = u.id_university
        WHERE cd.id_competition = :competitionId
        ORDER BY cd.discipline_name, dt.participant_gender, rp.rank_place
    """, nativeQuery = true)
    List<Object[]> findProtocols(@Param("competitionId") Integer competitionId);
}