package ua.edu.viti.military.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import ua.edu.viti.military.dto.request.MilitaryUnitCreateDTO;
import ua.edu.viti.military.dto.request.MilitaryUnitUpdateDTO;
import ua.edu.viti.military.dto.response.MilitaryUnitResponseDTO;
import ua.edu.viti.military.entity.MilitaryUnit;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MilitaryUnitMapper {

    // --- Entity -> DTO ---
    @Mapping(source = "unitType.name", target = "unitTypeName")
    @Mapping(source = "parentUnit.name", target = "parentUnitName")
    MilitaryUnitResponseDTO toDTO(MilitaryUnit entity);

    List<MilitaryUnitResponseDTO> toDTOList(List<MilitaryUnit> entities);

    // --- DTO -> Entity (Create) ---
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "unitType", ignore = true)
    @Mapping(target = "parentUnit", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    // РЯДКИ З currentStrength та commander ВИДАЛЕНІ
    MilitaryUnit toEntity(MilitaryUnitCreateDTO dto);

    // --- DTO -> Entity (Update) ---
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "unitType", ignore = true)
    @Mapping(target = "parentUnit", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    // РЯДКИ З currentStrength та commander ВИДАЛЕНІ
    void updateEntityFromDTO(MilitaryUnitUpdateDTO dto, @MappingTarget MilitaryUnit entity);
}