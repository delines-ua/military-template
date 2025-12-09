package ua.edu.viti.military.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.edu.viti.military.dto.request.UnitTypeCreateDTO;
import ua.edu.viti.military.dto.response.UnitTypeResponseDTO;
import ua.edu.viti.military.entity.UnitType;
import ua.edu.viti.military.repository.UnitTypeRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UnitTypeService {

    private final UnitTypeRepository unitTypeRepository;

    @Transactional
    public UnitTypeResponseDTO create(UnitTypeCreateDTO dto) {
        if (unitTypeRepository.existsByCode(dto.getCode())) {
            throw new RuntimeException("Тип підрозділу з таким кодом вже існує");
        }

        UnitType type = new UnitType();
        type.setName(dto.getName());
        type.setCode(dto.getCode());
        type.setDescription(dto.getDescription());
        type.setHierarchyLevel(dto.getHierarchyLevel());
        type.setTypicalSize(dto.getTypicalSize());

        UnitType saved = unitTypeRepository.save(type);
        return toDTO(saved);
    }

    public List<UnitTypeResponseDTO> getAll() {
        return unitTypeRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    private UnitTypeResponseDTO toDTO(UnitType entity) {
        UnitTypeResponseDTO dto = new UnitTypeResponseDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setCode(entity.getCode());
        dto.setDescription(entity.getDescription());
        dto.setHierarchyLevel(entity.getHierarchyLevel());
        dto.setTypicalSize(entity.getTypicalSize());
        dto.setCreatedAt(entity.getCreatedAt());
        return dto;
    }
}