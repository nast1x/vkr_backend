package com.ssau.kurs_back.service;

import com.ssau.kurs_back.dto.competition.*;
import com.ssau.kurs_back.entity.Competition;
import com.ssau.kurs_back.entity.ref.CompetitionType;
import com.ssau.kurs_back.entity.ref.SportFacility;
import com.ssau.kurs_back.entity.ref.SportType;
import com.ssau.kurs_back.exception.ResourceNotFoundException;
import com.ssau.kurs_back.repository.CompetitionRepository;
import com.ssau.kurs_back.repository.ref.CompetitionTypeRepository;
import com.ssau.kurs_back.repository.ref.SportFacilityRepository;
import com.ssau.kurs_back.repository.ref.SportTypeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class CompetitionServiceTest {

    @Mock private CompetitionRepository competitionRepository;
    @Mock private CompetitionTypeRepository competitionTypeRepository;
    @Mock private SportFacilityRepository sportFacilityRepository;
    @Mock private SportTypeRepository sportTypeRepository;

    
    @InjectMocks
    private CompetitionService competitionService;

    private Competition competition;
    private CompetitionType competitionType;
    private SportFacility sportFacility;
    private SportType sportType;

    @BeforeEach
    void setUp() {
        
        competitionType = new CompetitionType();
        competitionType.setIdCompetitionType(1);
        competitionType.setName("Тестовый тип");

        sportFacility = new SportFacility();
        sportFacility.setIdSportFacility(1);
        sportFacility.setCity("Самара");
        sportFacility.setStreet("Московское шоссе");

        sportType = new SportType();
        sportType.setIdSportType(1);
        sportType.setName("Легкая атлетика");

        competition = Competition.builder()
                .idCompetition(10)
                .name("Универсиада")
                .startDate(LocalDate.of(2026, 5, 10))
                .endDate(LocalDate.of(2026, 5, 12))
                .competitionType(competitionType)
                .sportFacility(sportFacility)
                .sportType(sportType)
                .organizer("ССАУ")
                .description("Тестовое описание")
                .build();
    }

    @Test
    void testFindAll() {
        when(competitionRepository.findAll()).thenReturn(List.of(competition));

        List<CompetitionResponseDto> result = competitionService.findAll();

        assertEquals(1, result.size());
        assertEquals("Универсиада", result.get(0).getName());
        assertEquals("Самара", result.get(0).getSportFacilityCity());
    }

    @Test
    void testFindById_Success() {
        when(competitionRepository.findById(10)).thenReturn(Optional.of(competition));

        CompetitionResponseDto result = competitionService.findById(10);

        assertNotNull(result);
        assertEquals(10, result.getIdCompetition());
        assertEquals("Тестовый тип", result.getCompetitionTypeName());
    }

    @Test
    void testFindById_NotFound() {
        when(competitionRepository.findById(99)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> competitionService.findById(99));
    }

    @Test
    void testCreate_Success() {
        CompetitionCreateDto createDto = new CompetitionCreateDto();
        createDto.setName("Новое соревнование");
        createDto.setCompetitionTypeId(1);
        createDto.setSportFacilityId(1);
        createDto.setSportTypeId(1);

        when(competitionTypeRepository.findById(1)).thenReturn(Optional.of(competitionType));
        when(sportFacilityRepository.findById(1)).thenReturn(Optional.of(sportFacility));
        when(sportTypeRepository.findById(1)).thenReturn(Optional.of(sportType));

        
        when(competitionRepository.save(any(Competition.class))).thenReturn(competition);

        CompetitionResponseDto result = competitionService.create(createDto);

        assertNotNull(result);
        assertEquals("Универсиада", result.getName()); 
        verify(competitionRepository, times(1)).save(any(Competition.class));
    }

    @Test
    void testUpdate_Success() {
        CompetitionUpdateDto updateDto = new CompetitionUpdateDto();
        updateDto.setName("Обновленное название");

        when(competitionRepository.findById(10)).thenReturn(Optional.of(competition));
        when(competitionRepository.save(any(Competition.class))).thenReturn(competition);

        CompetitionResponseDto result = competitionService.update(10, updateDto);

        assertNotNull(result);
        assertEquals("Обновленное название", competition.getName()); 
        verify(competitionRepository).save(competition);
    }

    @Test
    void testDeleteById_Success() {
        when(competitionRepository.existsById(10)).thenReturn(true);

        competitionService.deleteById(10);

        verify(competitionRepository, times(1)).deleteById(10);
    }

    @Test
    void testDeleteById_NotFound() {
        when(competitionRepository.existsById(99)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> competitionService.deleteById(99));
        verify(competitionRepository, never()).deleteById(anyInt());
    }

    @Test
    void testFindCompetitionDetail_Success() {
        
        Object[] basicInfo = {
                10, "Детализированное соревнование", "Международный", "Плавание", "Москва",
                java.sql.Date.valueOf("2026-06-01"), java.sql.Date.valueOf("2026-06-05"),
                1, "Бассейн", "Ул. Водная 1", "photo.png", "Федерация плавания"
        };

        Object[] bestResultRow = { 100, "Иван Иванов", "avatar.png", "МГУ", "00:22.50", "Вольный стиль 50м" };
        Object[] protocolRow = { "Вольный стиль 50м", "Мужчины", 100, "Иван Иванов", "МГУ", "00:22.50", 1 };


        when(competitionRepository.findCompetitionDetail(10))
                .thenReturn(Optional.of((Object) basicInfo));

        
        
        when(competitionRepository.findBestResults(10))
                .thenReturn(java.util.Collections.singletonList(bestResultRow));

        when(competitionRepository.findProtocols(10))
                .thenReturn(java.util.Collections.singletonList(protocolRow));

        
        CompetitionDetailDto result = competitionService.findCompetitionDetail(10);

        assertNotNull(result);
        assertEquals("Детализированное соревнование", result.getName());
        assertEquals("Международный", result.getCompetitionLevel());
        assertEquals("Плавание", result.getSportType());

        
        assertEquals(1, result.getBestResults().size());
        assertEquals("Иван Иванов", result.getBestResults().get(0).getAthleteName());

        
        assertEquals(1, result.getProtocols().size());
        assertEquals("Вольный стиль 50м", result.getProtocols().get(0).getDiscipline());
        assertEquals("Мужчины", result.getProtocols().get(0).getType());
        assertEquals(1, result.getProtocols().get(0).getResults().size());
        assertEquals(1, result.getProtocols().get(0).getResults().get(0).getRankPlace());
    }
}