package ua.edu.viti.military.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import ua.edu.viti.military.entity.MovementType;

@Data
public class MovementRequestDTO {

    @NotNull(message = "ID солдата обов'язковий")
    private Long personnelId;

    @NotNull(message = "Тип переміщення обов'язковий")
    private MovementType type; // TRANSFER, PROMOTION...

    private Long toUnitId; // Куди переводимо (може бути null, якщо звільнення)

    private String notes; // Номер наказу, причина
}