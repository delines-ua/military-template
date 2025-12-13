package ua.edu.viti.military.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import ua.edu.viti.military.dto.request.UnitTypeCreateDTO;
import ua.edu.viti.military.dto.request.UnitTypeUpdateDTO;
import ua.edu.viti.military.dto.response.UnitTypeResponseDTO;
import ua.edu.viti.military.entity.UnitType;

import java.util.List;

// componentModel = "spring" дозволяє інжектити цей маппер через @RequiredArgsConstructor
@Mapper(componentModel = "spring")
public interface UnitTypeMapper {

    // Entity -> DTO
    UnitTypeResponseDTO toDTO(UnitType entity);

    // List<Entity> -> List<DTO>
    List<UnitTypeResponseDTO> toDTOList(List<UnitType> entities);

    // DTO -> Entity (Створення)
    UnitType toEntity(UnitTypeCreateDTO dto);

    // DTO -> Entity (Оновлення)
    // @MappingTarget каже: "Візьми існуючий entity і онови в ньому поля з dto"
    void updateEntityFromDTO(UnitTypeUpdateDTO dto, @MappingTarget UnitType entity);
}