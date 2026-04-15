package com.ssau.kurs_back.service;

import com.ssau.kurs_back.dto.competition.CompetitionCreateDto;
import com.ssau.kurs_back.dto.competition.CompetitionDetailDto;
import com.ssau.kurs_back.dto.competition.CompetitionListDto;
import com.ssau.kurs_back.dto.competition.CompetitionResponseDto;
import com.ssau.kurs_back.dto.competition.CompetitionUpdateDto;
import com.ssau.kurs_back.entity.Competition;
import com.ssau.kurs_back.entity.ref.CompetitionType;
import com.ssau.kurs_back.entity.ref.SportFacility;
import com.ssau.kurs_back.entity.ref.SportType;
import com.ssau.kurs_back.exception.ResourceNotFoundException;
import com.ssau.kurs_back.repository.CompetitionRepository;
import com.ssau.kurs_back.repository.ref.CompetitionTypeRepository;
import com.ssau.kurs_back.repository.ref.SportFacilityRepository;
import com.ssau.kurs_back.repository.ref.SportTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CompetitionService {

    private final CompetitionRepository repository;
    private final CompetitionTypeRepository competitionTypeRepository;
    private final SportFacilityRepository sportFacilityRepository;
    private final SportTypeRepository sportTypeRepository;

    public List<CompetitionResponseDto> findAll() {
        return repository.findAll().stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }

    public List<CompetitionListDto> findCompetitionList() {
        return repository.findCompetitionList();
    }

    public CompetitionDetailDto findCompetitionDetail(Integer competitionId) {

        Object result = repository.findCompetitionDetail(competitionId)
                .orElseThrow(() -> new ResourceNotFoundException("Соревнование", competitionId));
        Object[] basicInfo = (Object[]) result;

        List<Object[]> bestResultsData = repository.findBestResults(competitionId);
        List<CompetitionDetailDto.BestResultDto> bestResults = bestResultsData.stream()
                .map(row -> CompetitionDetailDto.BestResultDto.builder()
                        .athleteId(convertToInt(row[0]))
                        .athleteName((String) row[1])
                        .athleteAvatar(row[2] != null ? (String) row[2] : "/assets/images/avatar-placeholder.png")
                        .university((String) row[3])
                        .result((String) row[4])
                        .discipline(row[5] != null ? row[5].toString() : "")
                        .build())
                .collect(Collectors.toList());

        List<Object[]> protocolsData = repository.findProtocols(competitionId);
        Map<String, Map<String, List<CompetitionDetailDto.ProtocolResultDto>>> protocolMap = protocolsData.stream()
                .filter(row -> row[2] != null)
                .collect(Collectors.groupingBy(
                        row -> (String) row[0],
                        Collectors.groupingBy(
                                row -> (String) row[1],
                                Collectors.mapping(row -> CompetitionDetailDto.ProtocolResultDto.builder()
                                        .athleteId(convertToInt(row[2]))
                                        .athleteName((String) row[3])
                                        .university((String) row[4])
                                        .result((String) row[5])
                                        .rankPlace(convertToInt(row[6]))
                                        .build(), Collectors.toList())
                        )
                ));

        List<CompetitionDetailDto.ProtocolDto> protocols = protocolMap.entrySet().stream()
                .map(disciplineEntry -> {
                    List<CompetitionDetailDto.ProtocolResultDto> allResults = new ArrayList<>();
                    String typeDescription = "";

                    for (Map.Entry<String, List<CompetitionDetailDto.ProtocolResultDto>> genderEntry :
                            disciplineEntry.getValue().entrySet()) {
                        allResults.addAll(genderEntry.getValue());
                        if (!typeDescription.isEmpty()) typeDescription += ", ";
                        typeDescription += genderEntry.getKey();
                    }

                    return CompetitionDetailDto.ProtocolDto.builder()
                            .discipline(disciplineEntry.getKey())
                            .type(typeDescription)
                            .results(allResults)
                            .build();
                })
                .collect(Collectors.toList());

        return CompetitionDetailDto.builder()
                .idCompetition(convertToInt(basicInfo[0]))
                .name((String) basicInfo[1])
                .competitionLevel((String) basicInfo[2])
                .sportType((String) basicInfo[3])
                .city((String) basicInfo[4])
                .startDate(convertToLocalDate(basicInfo[5]))
                .endDate(convertToLocalDate(basicInfo[6]))
                .venue(CompetitionDetailDto.VenueDto.builder()
                        .id(convertToInt(basicInfo[7]))
                        .name((String) basicInfo[8])
                        .address((String) basicInfo[9])
                        .photo((String) basicInfo[10])
                        .build())
                .organizer((String) basicInfo[11])
                .bestResults(bestResults)
                .protocols(protocols)
                .build();
    }

    public CompetitionResponseDto findById(Integer id) {
        Competition competition = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Соревнование", id));
        return toResponseDto(competition);
    }

    public List<CompetitionResponseDto> findBySportTypeId(Integer sportTypeId) {
        return repository.findBySportTypeIdSportType(sportTypeId).stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }

    public List<CompetitionResponseDto> findByCompetitionTypeId(Integer competitionTypeId) {
        return repository.findByCompetitionTypeIdCompetitionType(competitionTypeId).stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }

    public List<CompetitionResponseDto> findBySportFacilityId(Integer sportFacilityId) {
        return repository.findBySportFacilityIdSportFacility(sportFacilityId).stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }

    public List<CompetitionResponseDto> findByDateRange(LocalDate startDate, LocalDate endDate) {
        return repository.findByStartDateBetween(startDate, endDate).stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }

    public List<CompetitionResponseDto> findUpcoming() {
        return repository.findByStartDateAfter(LocalDate.now()).stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }

    public List<CompetitionResponseDto> searchByOrganizer(String organizer) {
        return repository.findByOrganizerContainingIgnoreCase(organizer).stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public CompetitionResponseDto create(CompetitionCreateDto dto) {
        CompetitionType competitionType = competitionTypeRepository.findById(dto.getCompetitionTypeId())
                .orElseThrow(() -> new ResourceNotFoundException("Тип соревнования", dto.getCompetitionTypeId()));

        SportFacility sportFacility = sportFacilityRepository.findById(dto.getSportFacilityId())
                .orElseThrow(() -> new ResourceNotFoundException("Спортивный объект", dto.getSportFacilityId()));

        SportType sportType = sportTypeRepository.findById(dto.getSportTypeId())
                .orElseThrow(() -> new ResourceNotFoundException("Вид спорта", dto.getSportTypeId()));

        Competition competition = Competition.builder()
                .name(dto.getName())
                .startDate(dto.getStartDate())
                .endDate(dto.getEndDate())
                .competitionType(competitionType)
                .sportFacility(sportFacility)
                .organizer(dto.getOrganizer())
                .sportType(sportType)
                .description(dto.getDescription())
                .build();

        return toResponseDto(repository.save(competition));
    }

    @Transactional
    public CompetitionResponseDto update(Integer id, CompetitionUpdateDto dto) {
        Competition competition = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Соревнование", id));

        if (dto.getName() != null) competition.setName(dto.getName());
        if (dto.getStartDate() != null) competition.setStartDate(dto.getStartDate());
        if (dto.getEndDate() != null) competition.setEndDate(dto.getEndDate());

        if (dto.getCompetitionTypeId() != null) {
            CompetitionType competitionType = competitionTypeRepository.findById(dto.getCompetitionTypeId())
                    .orElseThrow(() -> new ResourceNotFoundException("Тип соревнования", dto.getCompetitionTypeId()));
            competition.setCompetitionType(competitionType);
        }

        if (dto.getSportFacilityId() != null) {
            SportFacility sportFacility = sportFacilityRepository.findById(dto.getSportFacilityId())
                    .orElseThrow(() -> new ResourceNotFoundException("Спортивный объект", dto.getSportFacilityId()));
            competition.setSportFacility(sportFacility);
        }

        if (dto.getOrganizer() != null) competition.setOrganizer(dto.getOrganizer());

        if (dto.getSportTypeId() != null) {
            SportType sportType = sportTypeRepository.findById(dto.getSportTypeId())
                    .orElseThrow(() -> new ResourceNotFoundException("Вид спорта", dto.getSportTypeId()));
            competition.setSportType(sportType);
        }

        if (dto.getDescription() != null) competition.setDescription(dto.getDescription());

        return toResponseDto(repository.save(competition));
    }

    @Transactional
    public void deleteById(Integer id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Соревнование", id);
        }
        repository.deleteById(id);
    }

    private CompetitionResponseDto toResponseDto(Competition competition) {
        return CompetitionResponseDto.builder()
                .idCompetition(competition.getIdCompetition())
                .name(competition.getName())
                .startDate(competition.getStartDate())
                .endDate(competition.getEndDate())
                .competitionTypeId(competition.getCompetitionType().getIdCompetitionType())
                .competitionTypeName(competition.getCompetitionType().getName())
                .sportFacilityId(competition.getSportFacility().getIdSportFacility())
                .sportFacilityCity(competition.getSportFacility().getCity())
                .sportFacilityStreet(competition.getSportFacility().getStreet())
                .organizer(competition.getOrganizer())
                .sportTypeId(competition.getSportType().getIdSportType())
                .sportTypeName(competition.getSportType().getName())
                .description(competition.getDescription())
                .build();
    }

    private Integer convertToInt(Object value) {
        if (value == null) return 0;
        if (value instanceof Number) return ((Number) value).intValue();
        return Integer.parseInt(value.toString());
    }

    private LocalDate convertToLocalDate(Object value) {
        if (value == null) return null;
        if (value instanceof LocalDate) return (LocalDate) value;
        if (value instanceof java.sql.Date) return ((java.sql.Date) value).toLocalDate();
        return LocalDate.parse(value.toString());
    }
}