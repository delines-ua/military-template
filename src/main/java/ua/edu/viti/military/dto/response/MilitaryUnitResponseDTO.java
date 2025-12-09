package ua.edu.viti.military.dto.response;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class MilitaryUnitResponseDTO {
    private Long id;
    private String name;
    private String code;

    private String unitTypeName; // Назва типу ("Рота")
    private String parentUnitName; // Назва батька ("1-й Батальйон")

    private String location;
    private LocalDateTime createdAt;
}