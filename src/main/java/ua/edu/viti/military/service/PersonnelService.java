package ua.edu.viti.military.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.edu.viti.military.dto.request.PersonnelCreateDTO;
import ua.edu.viti.military.dto.request.PersonnelUpdateDTO;
import ua.edu.viti.military.dto.response.PersonnelResponseDTO;
import ua.edu.viti.military.entity.MilitaryUnit;
import ua.edu.viti.military.entity.Personnel;
import ua.edu.viti.military.exception.ResourceNotFoundException;
import ua.edu.viti.military.repository.MilitaryUnitRepository;
import ua.edu.viti.military.repository.PersonnelRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class PersonnelService {

    private final PersonnelRepository personnelRepository;
    private final MilitaryUnitRepository militaryUnitRepository;

    // --- CREATE (Створення) ---
    @Transactional
    public PersonnelResponseDTO create(PersonnelCreateDTO dto) {
        log.info("Створення військового з ID: {}", dto.getMilitaryId());

        // 1. Перевірка на дублікат
        if (personnelRepository.existsByMilitaryId(dto.getMilitaryId())) {
            throw new RuntimeException("Військовий з таким ID вже існує: " + dto.getMilitaryId());
        }

        // 2. Створення Entity
        Personnel personnel = new Personnel();
        personnel.setMilitaryId(dto.getMilitaryId());
        personnel.setFirstName(dto.getFirstName());
        personnel.setLastName(dto.getLastName());
        personnel.setMiddleName(dto.getMiddleName());
        personnel.setRank(dto.getRank());
        personnel.setSpecialization(dto.getSpecialization());
        personnel.setContractStartDate(dto.getContractStartDate());
        personnel.setContractEndDate(dto.getContractEndDate());
        personnel.setSecurityClearance(dto.getSecurityClearance());
        personnel.setMedicalCategory(dto.getMedicalCategory());
        personnel.setPhoneNumber(dto.getPhoneNumber());
        personnel.setEmail(dto.getEmail());

        // 3. Пошук підрозділу (якщо передали ID)
        if (dto.getUnitId() != null) {
            MilitaryUnit unit = militaryUnitRepository.findById(dto.getUnitId())
                    .orElseThrow(() -> new ResourceNotFoundException("Підрозділ не знайдено з ID: " + dto.getUnitId()));
            personnel.setUnit(unit);
        }

        // 4. Збереження
        Personnel saved = personnelRepository.save(personnel);
        log.info("Військового збережено з ID: {}", saved.getId());

        return toResponseDTO(saved);
    }
    @Transactional
    public PersonnelResponseDTO update(Long id, PersonnelUpdateDTO dto) {
        log.info("Оновлення даних військового з ID: {}", id);

        // 1. Знаходимо існуючий запис
        Personnel personnel = personnelRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Військового з ID " + id + " не знайдено"));

        // 2. Оновлюємо поля, ТІЛЬКИ якщо вони прийшли (не null)
        if (dto.getFirstName() != null) personnel.setFirstName(dto.getFirstName());
        if (dto.getLastName() != null) personnel.setLastName(dto.getLastName());
        if (dto.getMiddleName() != null) personnel.setMiddleName(dto.getMiddleName());
        if (dto.getRank() != null) personnel.setRank(dto.getRank());
        if (dto.getSpecialization() != null) personnel.setSpecialization(dto.getSpecialization());
        if (dto.getContractEndDate() != null) personnel.setContractEndDate(dto.getContractEndDate());
        if (dto.getSecurityClearance() != null) personnel.setSecurityClearance(dto.getSecurityClearance());
        if (dto.getPhoneNumber() != null) personnel.setPhoneNumber(dto.getPhoneNumber());
        if (dto.getEmail() != null) personnel.setEmail(dto.getEmail());

        // 3. Якщо змінюємо підрозділ
        if (dto.getUnitId() != null) {
            MilitaryUnit unit = militaryUnitRepository.findById(dto.getUnitId())
                    .orElseThrow(() -> new ResourceNotFoundException("Підрозділ не знайдено"));
            personnel.setUnit(unit);
        }

        // 4. Зберігаємо (Hibernate сам зробить update)
        return toResponseDTO(personnelRepository.save(personnel));
    }

    // --- READ (Отримати по ID) ---
    public PersonnelResponseDTO getById(Long id) {
        Personnel personnel = personnelRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Військового не знайдено з ID: " + id));
        return toResponseDTO(personnel);
    }

    // --- READ (Отримати всіх) ---
    public List<PersonnelResponseDTO> getAll() {
        return personnelRepository.findAll().stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }


    // --- Mapper (Entity -> DTO) ---
    private PersonnelResponseDTO toResponseDTO(Personnel entity) {
        PersonnelResponseDTO dto = new PersonnelResponseDTO();
        dto.setId(entity.getId());
        dto.setMilitaryId(entity.getMilitaryId());

        String fullName = entity.getLastName() + " " + entity.getFirstName();
        if (entity.getMiddleName() != null) {
            fullName += " " + entity.getMiddleName();
        }
        dto.setFullName(fullName);

        dto.setRank(entity.getRank());
        dto.setSpecialization(entity.getSpecialization());

        if (entity.getUnit() != null) {
            dto.setUnitName(entity.getUnit().getName());
        }

        dto.setContractEndDate(entity.getContractEndDate());
        dto.setSecurityClearance(entity.getSecurityClearance());
        dto.setCreatedAt(entity.getCreatedAt());

        return dto;
    }
}