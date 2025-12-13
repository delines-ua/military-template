package ua.edu.viti.military.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named; // Додали імпорт
import ua.edu.viti.military.dto.request.PersonnelCreateDTO;
import ua.edu.viti.military.dto.request.PersonnelUpdateDTO;
import ua.edu.viti.military.dto.response.PersonnelResponseDTO;
import ua.edu.viti.military.entity.Personnel;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PersonnelMapper {

    // --- Entity -> DTO ---
    @Mapping(source = "unit.name", target = "unitName")
    // Замість складного виразу викликаємо метод знизу
    @Mapping(target = "fullName", source = "entity", qualifiedByName = "calculateFullName")
    PersonnelResponseDTO toDTO(Personnel entity);

    List<PersonnelResponseDTO> toDTOList(List<Personnel> entities);

    // --- DTO -> Entity (Create) ---
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "unit", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Personnel toEntity(PersonnelCreateDTO dto);

    // --- DTO -> Entity (Update) ---
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "unit", ignore = true)
    @Mapping(target = "militaryId", ignore = true)
    @Mapping(target = "contractStartDate", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntityFromDTO(PersonnelUpdateDTO dto, @MappingTarget Personnel entity);

    // Допоміжний метод для склеювання імені (Java код замість виразів в анотаціях)
    @Named("calculateFullName")
    default String calculateFullName(Personnel person) {
        if (person == null) return null;

        StringBuilder sb = new StringBuilder();
        if (person.getLastName() != null) sb.append(person.getLastName());
        if (person.getFirstName() != null) sb.append(" ").append(person.getFirstName());
        if (person.getMiddleName() != null && !person.getMiddleName().isEmpty()) {
            sb.append(" ").append(person.getMiddleName());
        }
        return sb.toString().trim();
    }
}