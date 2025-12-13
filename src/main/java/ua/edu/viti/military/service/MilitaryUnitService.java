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
import ua.edu.viti.military.mapper.MilitaryUnitMapper;
import ua.edu.viti.military.repository.MilitaryUnitRepository;
import ua.edu.viti.military.repository.UnitTypeRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MilitaryUnitService {

    private final MilitaryUnitRepository militaryUnitRepository;
    private final UnitTypeRepository unitTypeRepository;
    private final MilitaryUnitMapper unitMapper; // Використовуємо новий мапер

    @Transactional
    public MilitaryUnitResponseDTO create(MilitaryUnitCreateDTO dto) {
        if (militaryUnitRepository.findByCode(dto.getCode()).isPresent()) {
            throw new RuntimeException("Підрозділ з таким кодом вже існує");
        }

        // 1. Маппимо прості поля
        MilitaryUnit unit = unitMapper.toEntity(dto);

        // 2. Дозаповнюємо зв'язки вручну (бо DTO має тільки ID)
        UnitType type = unitTypeRepository.findById(dto.getUnitTypeId())
                .orElseThrow(() -> new ResourceNotFoundException("Тип підрозділу не знайдено"));
        unit.setUnitType(type);

        if (dto.getParentUnitId() != null) {
            MilitaryUnit parent = militaryUnitRepository.findById(dto.getParentUnitId())
                    .orElseThrow(() -> new ResourceNotFoundException("Батьківський підрозділ не знайдено"));
            unit.setParentUnit(parent);
        }

        return unitMapper.toDTO(militaryUnitRepository.save(unit));
    }

    public List<MilitaryUnitResponseDTO> getAll() {
        return unitMapper.toDTOList(militaryUnitRepository.findAll());
    }

    public MilitaryUnitResponseDTO getById(Long id) {
        MilitaryUnit unit = militaryUnitRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Підрозділ не знайдено"));
        return unitMapper.toDTO(unit);
    }

    @Transactional
    public void delete(Long id) {
        if (!militaryUnitRepository.existsById(id)) {
            throw new ResourceNotFoundException("Підрозділ не знайдено");
        }
        militaryUnitRepository.deleteById(id);
    }

    @Transactional
    public MilitaryUnitResponseDTO update(Long id, MilitaryUnitUpdateDTO dto) {
        MilitaryUnit unit = militaryUnitRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Підрозділ не знайдено"));

        // Мапер оновлює прості поля
        unitMapper.updateEntityFromDTO(dto, unit);

        // Ручна обробка зв'язків
        if (dto.getUnitTypeId() != null) {
            UnitType newType = unitTypeRepository.findById(dto.getUnitTypeId())
                    .orElseThrow(() -> new ResourceNotFoundException("Тип підрозділу не знайдено"));
            unit.setUnitType(newType);
        }

        if (dto.getParentUnitId() != null) {
            if (dto.getParentUnitId().equals(id)) throw new RuntimeException("Циклічне посилання!");
            MilitaryUnit newParent = militaryUnitRepository.findById(dto.getParentUnitId())
                    .orElseThrow(() -> new ResourceNotFoundException("Батько не знайдений"));
            unit.setParentUnit(newParent);
        }

        return unitMapper.toDTO(militaryUnitRepository.save(unit));
    }
}