package com.ssau.kurs_back.service.ref;

import com.ssau.kurs_back.dto.sportfacility.SportFacilityDetailDto;
import com.ssau.kurs_back.dto.sportfacility.SportFacilityListDto;
import com.ssau.kurs_back.entity.ref.SportFacility;
import com.ssau.kurs_back.exception.ResourceNotFoundException;
import com.ssau.kurs_back.repository.ref.SportFacilityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SportFacilityService {

    private final SportFacilityRepository repository;

    public List<SportFacility> findAll() {
        return repository.findAll();
    }

    public Optional<SportFacility> findById(Integer id) {
        return repository.findById(id);
    }

    // ✅ Список объектов (кратко) для главной страницы объектов
    public List<SportFacilityListDto> findSportFacilityList() {
        List<Object[]> results = repository.findSportFacilityListNative();
        return results.stream()
                .map(row -> SportFacilityListDto.builder()
                        .idSportFacility(convertToInt(row[0]))
                        .name(row[1] != null ? row[1].toString() : "")
                        .imageLink(row[2] != null ? row[2].toString() : "/assets/images/facility-placeholder.png")
                        .description(row[3] != null ? row[3].toString() : "")
                        .city(row[4] != null ? row[4].toString() : "")
                        .address(row[5] != null ? row[5].toString() : "")
                        .sportTypes(row[6] != null ? row[6].toString() : "")
                        .build())
                .collect(Collectors.toList());
    }

    // ✅ Подробная информация об объекте (с исправленным маппингом и обработкой 404)
    public SportFacilityDetailDto findFacilityDetail(Integer id) {
        // 1. Базовая информация
        List<Object[]> basicInfoResults = repository.findFacilityBasicInfoNative(id);

        if (basicInfoResults == null || basicInfoResults.isEmpty()) {
            throw new ResourceNotFoundException("Спортивный объект", id);
        }

        Object[] basicInfo = basicInfoResults.get(0);

        // 2. Виды спорта на объекте
        List<String> sportsList = repository.findFacilitySports(id);

        // 3. Соревнования на объекте
        List<SportFacilityDetailDto.CompetitionDto> competitions = repository.findCompetitionsByFacility(id)
                .stream()
                .map(row -> SportFacilityDetailDto.CompetitionDto.builder()
                        .id(convertToInt(row[0]))
                        .name(row[1] != null ? row[1].toString() : "")
                        .date(row[2] != null ? row[2].toString() : "")
                        .build())
                .collect(Collectors.toList());

        // 4. Рекорды (Победители во всех дисциплинах)
        List<SportFacilityDetailDto.RecordDto> records = repository.findFacilityRecords(id)
                .stream()
                .map(row -> SportFacilityDetailDto.RecordDto.builder()
                        .athleteId(convertToInt(row[0]))
                        .athleteName(row[1] != null ? row[1].toString() : "")
                        .athleteAvatar(row[2] != null ? row[2].toString() : "/assets/images/avatar-placeholder.png")
                        .university(row[3] != null ? row[3].toString() : "")
                        .discipline(row[4] != null ? row[4].toString() : "")
                        .result(row[5] != null ? row[5].toString() : "")
                        .date(row[6] != null ? row[6].toString() : "")
                        .build())
                .collect(Collectors.toList());

        return SportFacilityDetailDto.builder()
                .id(convertToInt(basicInfo[0]))
                .name(basicInfo[1] != null ? basicInfo[1].toString() : "")
                .city(basicInfo[2] != null ? basicInfo[2].toString() : "")
                .address(basicInfo[3] != null ? basicInfo[3].toString() : "")
                .imageLink(basicInfo[4] != null ? basicInfo[4].toString() : "/assets/images/facility-placeholder.png")
                .description(basicInfo[5] != null ? basicInfo[5].toString() : "")
                .sportsList(sportsList)
                .competitions(competitions)
                .records(records)
                .build();
    }

    @Transactional
    public SportFacility save(SportFacility entity) {
        return repository.save(entity);
    }

    @Transactional
    public void deleteById(Integer id) {
        repository.deleteById(id);
    }

    @Transactional
    public SportFacility update(Integer id, SportFacility entity) {
        SportFacility existing = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Спортивный объект", id));

        if (entity.getName() != null) existing.setName(entity.getName());
        if (entity.getCity() != null) existing.setCity(entity.getCity());
        if (entity.getStreet() != null) existing.setStreet(entity.getStreet());
        if (entity.getStreetNumber() != null) existing.setStreetNumber(entity.getStreetNumber());
        if (entity.getDescription() != null) existing.setDescription(entity.getDescription());
        if (entity.getImageLink() != null) existing.setImageLink(entity.getImageLink());

        return repository.save(existing);
    }

    public List<SportFacility> searchByCity(String city) {
        return repository.findByCityContainingIgnoreCase(city);
    }

    // Утилитарный метод для безопасного преобразования в Integer
    private Integer convertToInt(Object value) {
        if (value == null) return 0;
        if (value instanceof Number) return ((Number) value).intValue();
        try {
            return Integer.parseInt(value.toString());
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}