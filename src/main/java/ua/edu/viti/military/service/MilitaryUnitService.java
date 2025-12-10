package ua.edu.viti.military.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.edu.viti.military.dto.request.MilitaryUnitCreateDTO;
import ua.edu.viti.military.dto.request.MilitaryUnitUpdateDTO;
import ua.edu.viti.military.dto.response.MilitaryUnitResponseDTO;
import ua.edu.viti.military.entity.MilitaryUnit;
import ua.edu.viti.military.entity.UnitType;
import ua.edu.viti.military.exception.ResourceNotFoundException;
import ua.edu.viti.military.repository.MilitaryUnitRepository;
import ua.edu.viti.military.repository.UnitTypeRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MilitaryUnitService {

    private final MilitaryUnitRepository militaryUnitRepository;
    private final UnitTypeRepository unitTypeRepository;

    @Transactional
    public MilitaryUnitResponseDTO create(MilitaryUnitCreateDTO dto) {
        if (militaryUnitRepository.findByCode(dto.getCode()).isPresent()) {
            throw new RuntimeException("Підрозділ з таким кодом вже існує");
        }

        MilitaryUnit unit = new MilitaryUnit();
        unit.setName(dto.getName());
        unit.setCode(dto.getCode());
        unit.setLocation(dto.getLocation());
        unit.setFormationDate(dto.getFormationDate());

        // 1. Знаходимо тип (Рота/Взвод)
        UnitType type = unitTypeRepository.findById(dto.getUnitTypeId())
                .orElseThrow(() -> new ResourceNotFoundException("Тип підрозділу не знайдено"));
        unit.setUnitType(type);

        // 2. Знаходимо батьківський підрозділ (якщо є)
        if (dto.getParentUnitId() != null) {
            MilitaryUnit parent = militaryUnitRepository.findById(dto.getParentUnitId())
                    .orElseThrow(() -> new ResourceNotFoundException("Батьківський підрозділ не знайдено"));
            unit.setParentUnit(parent);
        }

        MilitaryUnit saved = militaryUnitRepository.save(unit);
        return toDTO(saved);
    }

    public List<MilitaryUnitResponseDTO> getAll() {
        return militaryUnitRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
    // --- UPDATE (Оновлення) ---
    @Transactional
    public MilitaryUnitResponseDTO update(Long id, MilitaryUnitUpdateDTO dto) {
        // 1. Знаходимо підрозділ, який редагуємо
        MilitaryUnit unit = militaryUnitRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Підрозділ з ID " + id + " не знайдено"));

        // 2. Оновлюємо прості поля
        if (dto.getName() != null) unit.setName(dto.getName());
        if (dto.getCode() != null) {
            // Бажано перевірити на унікальність, якщо код змінюється
            if (!unit.getCode().equals(dto.getCode()) && militaryUnitRepository.findByCode(dto.getCode()).isPresent()) {
                throw new RuntimeException("Підрозділ з таким кодом вже існує");
            }
            unit.setCode(dto.getCode());
        }
        if (dto.getLocation() != null) unit.setLocation(dto.getLocation());
        if (dto.getFormationDate() != null) unit.setFormationDate(dto.getFormationDate());

        // 3. Змінюємо ТИП підрозділу (якщо передали новий ID)
        if (dto.getUnitTypeId() != null) {
            UnitType newType = unitTypeRepository.findById(dto.getUnitTypeId())
                    .orElseThrow(() -> new ResourceNotFoundException("Тип підрозділу не знайдено"));
            unit.setUnitType(newType);
        }

        // 4. Змінюємо БАТЬКІВСЬКИЙ підрозділ (перепідпорядкування)
        if (dto.getParentUnitId() != null) {
            // Перевірка, щоб підрозділ не став батьком сам собі
            if (dto.getParentUnitId().equals(id)) {
                throw new RuntimeException("Підрозділ не може бути батьком сам собі!");
            }

            MilitaryUnit newParent = militaryUnitRepository.findById(dto.getParentUnitId())
                    .orElseThrow(() -> new ResourceNotFoundException("Новий батьківський підрозділ не знайдено"));
            unit.setParentUnit(newParent);
        }

        // 5. Зберігаємо
        return toDTO(militaryUnitRepository.save(unit));
    }

    private MilitaryUnitResponseDTO toDTO(MilitaryUnit entity) {
        MilitaryUnitResponseDTO dto = new MilitaryUnitResponseDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setCode(entity.getCode());
        dto.setUnitTypeName(entity.getUnitType().getName());

        if (entity.getParentUnit() != null) {
            dto.setParentUnitName(entity.getParentUnit().getName());
        }

        dto.setLocation(entity.getLocation());
        dto.setCreatedAt(entity.getCreatedAt());
        return dto;
    }
    @Transactional
    public void delete(Long id) {
        if (!militaryUnitRepository.existsById(id)) {
            throw new ResourceNotFoundException("Підрозділ з ID " + id + " не знайдено");
        }
        militaryUnitRepository.deleteById(id);
    }
}