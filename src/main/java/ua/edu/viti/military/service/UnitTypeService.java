package ua.edu.viti.military.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.edu.viti.military.dto.request.UnitTypeCreateDTO;
import ua.edu.viti.military.dto.request.UnitTypeUpdateDTO;
import ua.edu.viti.military.dto.response.UnitTypeResponseDTO;
import ua.edu.viti.military.entity.UnitType;
import ua.edu.viti.military.exception.ResourceNotFoundException;
import ua.edu.viti.military.mapper.UnitTypeMapper; // <-- Імпорт маппера
import ua.edu.viti.military.repository.UnitTypeRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UnitTypeService {

    private final UnitTypeRepository unitTypeRepository;
    private final UnitTypeMapper unitTypeMapper; // <-- Інжектимо маппер

    // --- CREATE ---
    public UnitTypeResponseDTO create(UnitTypeCreateDTO dto) {
        if (unitTypeRepository.existsByCode(dto.getCode())) {
            throw new RuntimeException("Тип підрозділу з кодом " + dto.getCode() + " вже існує");
        }

        // БУЛО: UnitType type = new UnitType(); type.setName(...); ...
        // СТАЛО:
        UnitType type = unitTypeMapper.toEntity(dto);

        return unitTypeMapper.toDTO(unitTypeRepository.save(type));
    }

    // --- READ ONE ---
    public UnitTypeResponseDTO getById(Long id) {
        UnitType type = unitTypeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Тип не знайдено"));

        return unitTypeMapper.toDTO(type);
    }

    // --- READ ALL ---
    public List<UnitTypeResponseDTO> getAll() {
        return unitTypeMapper.toDTOList(unitTypeRepository.findAll());
    }

    // --- UPDATE ---
    @Transactional
    public UnitTypeResponseDTO update(Long id, UnitTypeUpdateDTO dto) {
        UnitType type = unitTypeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Тип не знайдено"));

        if (dto.getCode() != null && !dto.getCode().equals(type.getCode())) {
            if (unitTypeRepository.existsByCode(dto.getCode())) {
                throw new RuntimeException("Код вже зайнятий!");
            }
        }

        // БУЛО: Купа if (dto.getName() != null) type.setName(...)
        // СТАЛО: Один рядок
        unitTypeMapper.updateEntityFromDTO(dto, type);

        return unitTypeMapper.toDTO(unitTypeRepository.save(type));
    }

    // --- DELETE ---
    public void delete(Long id) {
        if (!unitTypeRepository.existsById(id)) {
            throw new ResourceNotFoundException("Тип не знайдено");
        }
        unitTypeRepository.deleteById(id);
    }

    // Метод toDTO внизу класу можна видалити, він більше не потрібен!
}