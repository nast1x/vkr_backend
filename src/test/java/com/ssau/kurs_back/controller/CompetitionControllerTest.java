package com.ssau.kurs_back.controller;

import com.ssau.kurs_back.dto.competition.CompetitionResponseDto;
import com.ssau.kurs_back.entity.Competition;
import com.ssau.kurs_back.entity.ref.CompetitionType;
import com.ssau.kurs_back.entity.ref.SportFacility;
import com.ssau.kurs_back.entity.ref.SportType;
import com.ssau.kurs_back.repository.CompetitionRepository;
import com.ssau.kurs_back.repository.ref.CompetitionTypeRepository;
import com.ssau.kurs_back.repository.ref.SportFacilityRepository;
import com.ssau.kurs_back.repository.ref.SportTypeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@Transactional // ВОТ ОНО! Главное исправление. Держит сессию и чистит БД.
class CompetitionControllerTest {

    @Autowired
    private CompetitionController competitionController;

    @Autowired
    private CompetitionRepository competitionRepository;

    @Autowired
    private CompetitionTypeRepository competitionTypeRepository;
    @Autowired
    private SportFacilityRepository sportFacilityRepository;
    @Autowired
    private SportTypeRepository sportTypeRepository;

    @BeforeEach
    public void before() {
        CompetitionType type = new CompetitionType();
        type.setName("Международные");
        type = competitionTypeRepository.save(type);

        SportFacility facility = new SportFacility();
        facility.setName("Стадион");
        facility.setCity("Москва");
        facility.setStreet("Ленина");
        facility.setStreetNumber("1");
        facility = sportFacilityRepository.save(facility);

        SportType sportType = new SportType();
        sportType.setName("Легкая атлетика");
        sportType = sportTypeRepository.save(sportType);

        Competition competition = new Competition();
        competition.setName("Тестовое соревнование");
        competition.setStartDate(LocalDate.parse("2026-07-01"));
        competition.setOrganizer("Тестовый организатор");

        // Связываем объекты
        competition.setCompetitionType(type);
        competition.setSportFacility(facility);
        competition.setSportType(sportType);

        competitionRepository.save(competition);
    }

    // Метод after() с deleteAllInBatch удален, так как @Transactional сделает это за нас!

    @Test
    void testGetAll() {
        ResponseEntity<List<CompetitionResponseDto>> response = competitionController.getAll();
        List<CompetitionResponseDto> competitions = response.getBody();

        assertNotNull(competitions);
        assertEquals(1, competitions.size());
        assertEquals("Тестовое соревнование", competitions.get(0).getName());
    }
}