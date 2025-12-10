package ua.edu.viti.military.dto.request;

import lombok.Data;

@Data
public class UnitTypeUpdateDTO {
    private String name;
    private String code; // Можна виправити помилку в коді
    private String description;
    private Integer hierarchyLevel;
    private Integer typicalSize;
}