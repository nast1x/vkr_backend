package com.ssau.kurs_back.repository.ref;

import com.ssau.kurs_back.entity.ref.SportFacility;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SportFacilityRepository extends JpaRepository<SportFacility, Integer> {

    List<SportFacility> findByCityContainingIgnoreCase(String city);

    // ✅ Список объектов (кратко)
    @Query(value = """
        SELECT 
            sf.id_sport_facility,
            sf.name,
            sf.image_link,
            sf.description,
            sf.city,
            CONCAT(sf.city, ', ', sf.street, ', д. ', sf.street_number) AS full_address,
            COALESCE(string_agg(DISTINCT st.name, ', '), '') AS sport_types
        FROM "Sport_Facility" sf
        LEFT JOIN "Competition" c ON c.id_sport_facility = sf.id_sport_facility
        LEFT JOIN "Sport_Type" st ON c.id_sport_type = st.id_sport_type
        GROUP BY sf.id_sport_facility, sf.name, sf.image_link, sf.description, sf.city, sf.street, sf.street_number
        ORDER BY sf.city
    """, nativeQuery = true)
    List<Object[]> findSportFacilityListNative();

    // ✅ Базовая информация об объекте
    @Query(value = """
        SELECT 
            sf.id_sport_facility,
            sf.name,
            sf.city,
            CONCAT(sf.street, ', д. ', sf.street_number) AS address,
            sf.image_link,
            sf.description
        FROM "Sport_Facility" sf
        WHERE sf.id_sport_facility = :id
    """, nativeQuery = true)
    List<Object[]> findFacilityBasicInfoNative(@Param("id") Integer id);

    // ✅ Виды спорта на объекте
    @Query(value = """
        SELECT DISTINCT st.name
        FROM "Sport_Type" st
        JOIN "Competition" c ON st.id_sport_type = c.id_sport_type
        WHERE c.id_sport_facility = :facilityId
    """, nativeQuery = true)
    List<String> findFacilitySports(@Param("facilityId") Integer facilityId);

    // ✅ Соревнования на объекте
    @Query(value = """
        SELECT 
            c.id_competition,
            c.name,
            TO_CHAR(c.start_date, 'DD.MM.YYYY') || 
                CASE 
                    WHEN c.end_date IS NOT NULL 
                    THEN ' - ' || TO_CHAR(c.end_date, 'DD.MM.YYYY') 
                    ELSE '' 
                END AS date_range
        FROM "Competition" c
        WHERE c.id_sport_facility = :facilityId
        ORDER BY c.start_date DESC
    """, nativeQuery = true)
    List<Object[]> findCompetitionsByFacility(@Param("facilityId") Integer facilityId);

    // ✅ ЛУЧШИЕ РЕЗУЛЬТАТЫ (РЕКОРДЫ ОБЪЕКТА)
    // Выводит все 1-е места во всех дисциплинах, привязанных к объекту
    @Query(value = """
        SELECT 
            usr.id_user,
            usr.last_name || ' ' || usr.first_name || ' ' || COALESCE(usr.middle_name, '') AS athlete_name,
            usr.image_link,
            u.short_name AS university,
            cd.discipline_name,
            rp.result_value,
            TO_CHAR(c.start_date, 'DD.MM.YYYY') AS competition_date
        FROM "Sport_Facility" sf
        JOIN "Competition" c ON sf.id_sport_facility = c.id_sport_facility
        JOIN "Competition_Discipline" cd ON c.id_competition = cd.id_competition
        JOIN "Result_Personal" rp ON cd.id_competition_discipline = rp.id_competition_discipline
        JOIN "Users" usr ON rp.id_user = usr.id_user
        JOIN "Education_Place" ep ON usr.id_user = ep.id_user
        JOIN "University" u ON ep.id_university = u.id_university
        WHERE sf.id_sport_facility = :facilityId
            AND rp.rank_place = 1
        ORDER BY c.start_date DESC, cd.discipline_name ASC
    """, nativeQuery = true)
    List<Object[]> findFacilityRecords(@Param("facilityId") Integer facilityId);
}