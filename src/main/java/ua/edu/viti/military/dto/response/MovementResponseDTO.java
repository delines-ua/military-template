package ua.edu.viti.military.dto.response;

import lombok.Data;
import ua.edu.viti.military.entity.MovementType;
import java.time.LocalDateTime;

@Data
public class MovementResponseDTO {
    private Long id;
    private String personnelName;
    private MovementType type;
    private String fromUnitName;
    private String toUnitName;
    private String notes;
    private String performedBy;
    private LocalDateTime performedAt;
}