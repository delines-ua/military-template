package ua.edu.viti.military.dto.response;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class UnitTypeResponseDTO {
    private Long id;
    private String name;
    private String code;
    private String description;
    private Integer hierarchyLevel;
    private Integer typicalSize;
    private LocalDateTime createdAt;
}