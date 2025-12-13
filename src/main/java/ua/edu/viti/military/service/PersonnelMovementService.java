package ua.edu.viti.military.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import ua.edu.viti.military.dto.request.MovementRequestDTO;
import ua.edu.viti.military.dto.response.MovementResponseDTO;
import ua.edu.viti.military.entity.*;
import ua.edu.viti.military.exception.ResourceNotFoundException;
import ua.edu.viti.military.repository.*;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PersonnelMovementService {

    private final PersonnelRepository personnelRepository;
    private final MilitaryUnitRepository militaryUnitRepository;
    private final PersonnelMovementRepository movementRepository;

    // --- TRANSFER (Переведення / Зарахування) ---
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public MovementResponseDTO transferPersonnel(MovementRequestDTO dto) {
        log.info("Операція переміщення: {} для солдата ID: {}", dto.getType(), dto.getPersonnelId());

        // 1. Знаходимо солдата
        Personnel personnel = personnelRepository.findById(dto.getPersonnelId())
                .orElseThrow(() -> new ResourceNotFoundException("Солдата не знайдено"));

        // Запам'ятовуємо назву старого підрозділу (для історії)
        String fromUnitName = (personnel.getUnit() != null) ? personnel.getUnit().getName() : "Резерв/Немає";

        // 2. Знаходимо новий підрозділ (Куди переводимо)
        // Якщо це звільнення (DISMISSAL), то toUnitId може бути null, треба це врахувати (але поки робимо для TRANSFER)
        MilitaryUnit toUnit = null;
        String toUnitName = "Резерв/Звільнено";

        if (dto.getToUnitId() != null) {
            toUnit = militaryUnitRepository.findById(dto.getToUnitId())
                    .orElseThrow(() -> new ResourceNotFoundException("Новий підрозділ не знайдено з ID: " + dto.getToUnitId()));
            toUnitName = toUnit.getName();
        }

        // 3. Оновлюємо дані солдата (реальне переміщення)
        personnel.setUnit(toUnit); // Якщо null - значить забрали з підрозділу
        personnelRepository.save(personnel);

        // 4. Створюємо запис в журналі (Movement Log)
        PersonnelMovement movement = new PersonnelMovement();
        movement.setPersonnel(personnel);

        // 🔥 ВИПРАВЛЕННЯ: Беремо тип із DTO, а не хардкодимо TRANSFER
        movement.setType(dto.getType());

        movement.setFromUnitName(fromUnitName);
        movement.setToUnitName(toUnitName);
        movement.setNotes(dto.getNotes());
        movement.setPerformedBy("admin"); // Тимчасово

        // ⚠️ УВАГА: Тут немає performedAt. Воно має заповнитись саме через анотації в Entity.

        PersonnelMovement savedMovement = movementRepository.save(movement);
        log.info("Запис в журнал створено з ID: {}", savedMovement.getId());

        return toDTO(savedMovement);
    }

    // --- Отримати історію ---
    public List<MovementResponseDTO> getHistory(Long personnelId) {
        return movementRepository.findByPersonnelIdOrderByPerformedAtDesc(personnelId)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    private MovementResponseDTO toDTO(PersonnelMovement entity) {
        MovementResponseDTO dto = new MovementResponseDTO();
        dto.setId(entity.getId());
        dto.setPersonnelName(entity.getPersonnel().getLastName() + " " + entity.getPersonnel().getFirstName());
        dto.setType(entity.getType());
        dto.setFromUnitName(entity.getFromUnitName());
        dto.setToUnitName(entity.getToUnitName());
        dto.setNotes(entity.getNotes());
        dto.setPerformedBy(entity.getPerformedBy());
        dto.setPerformedAt(entity.getPerformedAt());
        return dto;
    }
}