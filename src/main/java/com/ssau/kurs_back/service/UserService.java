package com.ssau.kurs_back.service;

import com.ssau.kurs_back.dto.user.*;
import com.ssau.kurs_back.entity.User;
import com.ssau.kurs_back.entity.ref.Role;
import com.ssau.kurs_back.exception.DuplicateResourceException;
import com.ssau.kurs_back.exception.ResourceNotFoundException;
import com.ssau.kurs_back.repository.UserRepository;
import com.ssau.kurs_back.repository.ref.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository repository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    private static final String UPLOAD_DIR = "uploads/photos/";

    public List<UserResponseDto> findAll() {
        return repository.findAll().stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }

    public UserResponseDto findById(Integer id) {
        User user = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Пользователь", id));
        return toResponseDto(user);
    }

    public UserResponseDto findByEmail(String email) {
        User user = repository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Пользователь с email", email));
        return toResponseDto(user);
    }

    public List<UserResponseDto> searchByLastName(String lastName) {
        return repository.findByLastNameContainingIgnoreCase(lastName).stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }

    public List<UserResponseDto> findTraineesByCoachId(Integer coachId) {
        if (!repository.existsById(coachId)) {
            throw new ResourceNotFoundException("Тренер", coachId);
        }
        return repository.findByCoachIdUser(coachId).stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }

    public List<TeamMemberDto> findTeamMembers() {
        return repository.findTeamMembers();
    }

    /**
     * Получение профиля пользователя
     */
    public UserProfileDto getUserProfile(Integer userId) {
        User user = repository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Пользователь", userId));
        return convertToProfileDto(user);
    }

    /**
     * Загрузка и обновление фото пользователя
     */
    @Transactional
    public UserProfileDto updateUserPhoto(Integer userId, MultipartFile file) {
        User user = repository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Пользователь", userId));

        try {
            // 1. Создаем директорию
            Path uploadPath = Paths.get(UPLOAD_DIR);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // 2. Генерируем уникальное имя файла
            String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
            Path filePath = uploadPath.resolve(fileName);

            // 3. Сохраняем файл
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            // 4. Обновляем путь в БД (используем imageLink, как в вашей сущности)
            String photoUrl = "/" + UPLOAD_DIR + fileName;
            user.setImageLink(photoUrl);
            User savedUser = repository.save(user);

            return convertToProfileDto(savedUser);
        } catch (IOException e) {
            throw new RuntimeException("Ошибка при сохранении изображения: " + e.getMessage());
        }
    }

    // ===== CREATE / UPDATE / DELETE =====

    @Transactional
    public UserResponseDto create(UserCreateDto dto) {
        if (repository.existsByEmail(dto.getEmail())) {
            throw new DuplicateResourceException("Пользователь с таким email уже существует", dto.getEmail());
        }

        Role role = roleRepository.findById(dto.getRoleId())
                .orElseThrow(() -> new ResourceNotFoundException("Роль", dto.getRoleId()));

        User coach = null;
        if (dto.getCoachId() != null) {
            coach = repository.findById(dto.getCoachId())
                    .orElseThrow(() -> new ResourceNotFoundException("Тренер", dto.getCoachId()));
        }

        User user = User.builder()
                .email(dto.getEmail())
                .password(passwordEncoder.encode(dto.getPassword()))
                .lastName(dto.getLastName())
                .firstName(dto.getFirstName())
                .middleName(dto.getMiddleName())
                .birthDate(dto.getBirthDate())
                .gender(dto.getGender())
                .imageLink(dto.getImageLink())
                .role(role)
                .coach(coach)
                .build();

        return toResponseDto(repository.save(user));
    }

    @Transactional
    public UserResponseDto update(Integer id, UserUpdateDto dto) {
        User user = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Пользователь", id));

        if (dto.getEmail() != null && !dto.getEmail().equals(user.getEmail())) {
            if (repository.existsByEmail(dto.getEmail())) {
                throw new DuplicateResourceException("Пользователь с таким email уже существует", dto.getEmail());
            }
            user.setEmail(dto.getEmail());
        }

        if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
            if (dto.getOldPassword() == null || !passwordEncoder.matches(dto.getOldPassword(), user.getPassword())) {
                throw new IllegalArgumentException("Текущий пароль введен неверно");
            }
            user.setPassword(passwordEncoder.encode(dto.getPassword()));
        }

        if (dto.getLastName() != null) user.setLastName(dto.getLastName());
        if (dto.getFirstName() != null) user.setFirstName(dto.getFirstName());
        if (dto.getMiddleName() != null) user.setMiddleName(dto.getMiddleName());
        if (dto.getBirthDate() != null) user.setBirthDate(dto.getBirthDate());
        if (dto.getGender() != null) user.setGender(dto.getGender());
        if (dto.getImageLink() != null) user.setImageLink(dto.getImageLink());

        if (dto.getRoleId() != null) {
            Role role = roleRepository.findById(dto.getRoleId())
                    .orElseThrow(() -> new ResourceNotFoundException("Роль", dto.getRoleId()));
            user.setRole(role);
        }

        if (dto.getCoachId() != null) {
            User coach = repository.findById(dto.getCoachId())
                    .orElseThrow(() -> new ResourceNotFoundException("Тренер", dto.getCoachId()));
            user.setCoach(coach);
        } else if (dto.getCoachId() == null) {
            user.setCoach(null);
        }

        return toResponseDto(repository.save(user));
    }


    @Transactional
    public UserResponseDto updateRole(Integer userId, Integer roleId) {
        User user = repository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Пользователь", userId));
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new ResourceNotFoundException("Роль", roleId));

        user.setRole(role);
        return toResponseDto(repository.save(user));
    }

    @Transactional
    public void deleteById(Integer id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Пользователь", id);
        }
        repository.deleteById(id);
    }

    // ===== ВСПОМОГАТЕЛЬНЫЕ МЕТОДЫ =====

    /**
     * Общий метод для сборки профиля (DTO)
     */
    private UserProfileDto convertToProfileDto(User user) {
        Integer userId = user.getIdUser();

        // Образование
        List<Object[]> educationResults = repository.findEducationPlace(userId);
        Integer universityId = null;
        String university = null;
        String faculty = null;
        Integer course = null;

        if (!educationResults.isEmpty()) {
            Object[] edu = educationResults.get(0);
            universityId = edu[0] != null ? ((Number) edu[0]).intValue() : null;
            university = (String) edu[1];
            faculty = (String) edu[2];
            course = edu[3] != null ? ((Number) edu[3]).intValue() : null;
        }

        // Спорт
        List<Object[]> sportResults = repository.findSportInfo(userId);
        List<UserProfileDto.UserSportDto> sportsList = new ArrayList<>();

        for (Object[] row : sportResults) {
            sportsList.add(UserProfileDto.UserSportDto.builder()
                    .sportName((String) row[0])
                    .rankName((String) row[1])
                    .dateReceived((LocalDate) row[2])
                    .build());
        }

        // Воспитанники (если тренер)
        List<UserProfileDto.TraineeDto> trainees = new ArrayList<>();
        if ("Coach".equals(user.getRole().getName())) {

            // Берем первый вид спорта тренера как основной для его подопечных (если список не пуст)
            String mainSport = sportsList.isEmpty() ? null : sportsList.get(0).getSportName();

            List<Object[]> traineesData = repository.findTraineesByCoachId(userId);
            for (Object[] row : traineesData) {
                trainees.add(UserProfileDto.TraineeDto.builder()
                        .id(convertToInt(row[0]))
                        .fullName((String) row[1])
                        .avatar(row[2] != null ? (String) row[2] : "/assets/images/avatar-placeholder.png")
                        .sport(mainSport)
                        .build());
            }
        }

        // Результаты (если спортсмен)
        List<UserProfileDto.PersonalRecordDto> records = new ArrayList<>();
        if ("Athlete".equals(user.getRole().getName())) {
            List<Object[]> recordsData = repository.findPersonalRecords(userId);
            for (Object[] row : recordsData) {
                records.add(UserProfileDto.PersonalRecordDto.builder()
                        .discipline((String) row[0])
                        .result((String) row[1])
                        .date((String) row[2])
                        .competitionId(convertToInt(row[3]))
                        .competitionName((String) row[4])
                        .build());
            }
        }

        return UserProfileDto.builder()
                .id(user.getIdUser())
                .fullName(buildFullName(user))
                .email(user.getEmail())
                .avatar(user.getImageLink() != null ? user.getImageLink() : "/assets/images/avatar-placeholder.png")
                .role(user.getRole().getName())
                .age(calculateAge(user.getBirthDate()))
                .birthDate(user.getBirthDate())
                .gender(user.getGender())
                .universityId(universityId)
                .university(university)
                .faculty(faculty)
                .course(course)
                .coachId(user.getCoach() != null ? user.getCoach().getIdUser() : null)
                .coachAvatar(user.getCoach() != null ?
                        (user.getCoach().getImageLink() != null ? user.getCoach().getImageLink() : "/assets/images/avatar-placeholder.png")
                        : null)
                .coachName(user.getCoach() != null ? buildFullName(user.getCoach()) : null)
                .sport(sportsList)
                .trainees(trainees)
                .records(records)
                .build();
    }

    private UserResponseDto toResponseDto(User user) {
        return UserResponseDto.builder()
                .idUser(user.getIdUser())
                .email(user.getEmail())
                .lastName(user.getLastName())
                .firstName(user.getFirstName())
                .middleName(user.getMiddleName())
                .birthDate(user.getBirthDate())
                .gender(user.getGender())
                .imageLink(user.getImageLink())
                .roleId(user.getRole().getIdRole())
                .roleName(user.getRole().getName())
                .coachId(user.getCoach() != null ? user.getCoach().getIdUser() : null)
                .coachName(user.getCoach() != null ? buildFullName(user.getCoach()) : null)
                .build();
    }

    private String buildFullName(User user) {
        if (user == null) return null;
        StringBuilder fullName = new StringBuilder();
        if (user.getLastName() != null) fullName.append(user.getLastName());
        if (user.getFirstName() != null) {
            if (fullName.length() > 0) fullName.append(" ");
            fullName.append(user.getFirstName());
        }
        if (user.getMiddleName() != null) {
            if (fullName.length() > 0) fullName.append(" ");
            fullName.append(user.getMiddleName());
        }
        return fullName.toString();
    }

    private Integer calculateAge(LocalDate birthDate) {
        if (birthDate == null) return null;
        return Period.between(birthDate, LocalDate.now()).getYears();
    }

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