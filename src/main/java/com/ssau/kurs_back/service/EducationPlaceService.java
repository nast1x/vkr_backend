package com.ssau.kurs_back.service;

import com.ssau.kurs_back.dto.education.EducationPlaceCreateDto;
import com.ssau.kurs_back.dto.education.EducationPlaceResponseDto;
import com.ssau.kurs_back.dto.education.EducationPlaceUpdateDto;
import com.ssau.kurs_back.entity.EducationPlace;
import com.ssau.kurs_back.entity.User;
import com.ssau.kurs_back.entity.ref.Major;
import com.ssau.kurs_back.entity.ref.University;
import com.ssau.kurs_back.repository.EducationPlaceRepository;
import com.ssau.kurs_back.repository.UserRepository;
import com.ssau.kurs_back.repository.ref.MajorRepository;
import com.ssau.kurs_back.repository.ref.UniversityRepository;
import com.ssau.kurs_back.exception.ResourceNotFoundException;
import com.ssau.kurs_back.exception.DuplicateResourceException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EducationPlaceService {

    private final EducationPlaceRepository repository;
    private final UserRepository userRepository;
    private final UniversityRepository universityRepository;
    private final MajorRepository majorRepository;

    public List<EducationPlaceResponseDto> findAll() {
        // 1. Получаем всех пользователей, у которых УЖЕ есть место обучения
        List<EducationPlaceResponseDto> places = repository.findAll().stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());

        // 2. Получаем пользователей БЕЗ места обучения и формируем для них "пустые" DTO
        List<User> usersWithoutEducation = userRepository.findUsersWithoutEducationPlace();
        List<EducationPlaceResponseDto> emptyPlaces = usersWithoutEducation.stream()
                .map(this::toEmptyResponseDto)
                .collect(Collectors.toList());

        // 3. Объединяем списки
        places.addAll(emptyPlaces);

        return places;
    }

    public EducationPlaceResponseDto findById(Integer id) {
        EducationPlace educationPlace = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Место обучения", id));
        return toResponseDto(educationPlace);
    }

    public List<EducationPlaceResponseDto> findByUserId(Integer userId) {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("Пользователь", userId);
        }
        return repository.findByUserIdUser(userId).stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }

    public List<EducationPlaceResponseDto> findByUniversityId(Integer universityId) {
        return repository.findByUniversityIdUniversity(universityId).stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }

    public List<EducationPlaceResponseDto> findByMajorId(Integer majorId) {
        return repository.findByMajorIdMajor(majorId).stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public EducationPlaceResponseDto create(EducationPlaceCreateDto dto) {
        // Проверка существования пользователя
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("Пользователь", dto.getUserId()));

        // Проверка существования вуза
        University university = universityRepository.findById(dto.getUniversityId())
                .orElseThrow(() -> new ResourceNotFoundException("Вуз", dto.getUniversityId()));

        // Проверка существования специальности
        Major major = majorRepository.findById(dto.getMajorId())
                .orElseThrow(() -> new ResourceNotFoundException("Специальность", dto.getMajorId()));

        // Проверка уникальности (пользователь + вуз)
        if (repository.existsByUserIdUserAndUniversityIdUniversity(dto.getUserId(), dto.getUniversityId())) {
            throw new DuplicateResourceException(
                    "Запись о обучении (пользователь + вуз)",
                    dto.getUserId() + " + " + dto.getUniversityId()
            );
        }

        EducationPlace educationPlace = EducationPlace.builder()
                .user(user)
                .university(university)
                .major(major)
                .courseYear(dto.getCourseYear())
                .build();

        return toResponseDto(repository.save(educationPlace));
    }

    @Transactional
    public EducationPlaceResponseDto update(Integer id, EducationPlaceUpdateDto dto) {
        EducationPlace educationPlace = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Место обучения", id));

        if (dto.getUniversityId() != null) {
            // ИСПРАВЛЕНИЕ: Проверяем на дубликат ТОЛЬКО если ВУЗ действительно изменился
            if (!dto.getUniversityId().equals(educationPlace.getUniversity().getIdUniversity())) {
                if (repository.existsByUserIdUserAndUniversityIdUniversity(
                        educationPlace.getUser().getIdUser(), dto.getUniversityId())) {
                    throw new DuplicateResourceException(
                            "Запись о обучении (пользователь + вуз)",
                            educationPlace.getUser().getIdUser() + " + " + dto.getUniversityId()
                    );
                }
                // Если проверки пройдены, находим новый ВУЗ и устанавливаем его
                University university = universityRepository.findById(dto.getUniversityId())
                        .orElseThrow(() -> new ResourceNotFoundException("Вуз", dto.getUniversityId()));
                educationPlace.setUniversity(university);
            }
        }

        if (dto.getMajorId() != null) {
            Major major = majorRepository.findById(dto.getMajorId())
                    .orElseThrow(() -> new ResourceNotFoundException("Специальность", dto.getMajorId()));
            educationPlace.setMajor(major);
        }

        // Обновляем курс (можно установить null, если передано пустое значение, либо обновить)
        if (dto.getCourseYear() != null) {
            educationPlace.setCourseYear(dto.getCourseYear());
        }

        return toResponseDto(repository.save(educationPlace));
    }

    @Transactional
    public void deleteById(Integer id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Место обучения", id);
        }
        repository.deleteById(id);
    }

    private EducationPlaceResponseDto toResponseDto(EducationPlace educationPlace) {
        return EducationPlaceResponseDto.builder()
                .idEducationPlace(educationPlace.getIdEducationPlace())
                .userId(educationPlace.getUser().getIdUser())
                .userName(educationPlace.getUser().getFirstName() + " " + educationPlace.getUser().getLastName())
                .roleId(educationPlace.getUser().getRole().getIdRole())
                .userRole(educationPlace.getUser().getRole().getName()) // ДОБАВЛЕНО
                .universityId(educationPlace.getUniversity().getIdUniversity())
                .universityName(educationPlace.getUniversity().getName())
                .universityShortName(educationPlace.getUniversity().getShortName())
                .majorId(educationPlace.getMajor().getIdMajor())
                .majorName(educationPlace.getMajor().getName())
                .majorCode(educationPlace.getMajor().getCode())
                .courseYear(educationPlace.getCourseYear())
                .build();
    }

    // ДОБАВЛЕНО: Метод для конвертации пользователя без места обучения в DTO
    private EducationPlaceResponseDto toEmptyResponseDto(User user) {
        return EducationPlaceResponseDto.builder()
                .idEducationPlace(null) // ID места обучения нет
                .userId(user.getIdUser())
                .userName(user.getFirstName() + " " + (user.getLastName() != null ? user.getLastName() : ""))
                .roleId(user.getRole().getIdRole())
                .userRole(user.getRole().getName())
                .universityId(null)
                .universityName(null)
                .universityShortName("—")
                .majorId(null)
                .majorName(null)
                .majorCode("—")
                .courseYear(null)
                .build();
    }
}