package ua.edu.viti.military.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UnitTypeCreateDTO {
    @NotBlank(message = "Назва типу обов'язкова")
    private String name; // "Механізована рота"

    @NotBlank(message = "Код типу обов'язковий")
    private String code; // "MECH_COMPANY"

    private String description;

    @NotNull(message = "Рівень ієрархії обов'язковий")
    private Integer hierarchyLevel;

    private Integer typicalSize;
}