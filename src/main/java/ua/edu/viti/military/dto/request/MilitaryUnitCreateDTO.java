package ua.edu.viti.military.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class MilitaryUnitCreateDTO {

    @NotBlank(message = "Назва підрозділу обов'язкова")
    private String name; // "1-ша механізована рота"

    @NotBlank(message = "Код підрозділу обов'язковий")
    private String code; // "1-MR"

    @NotNull(message = "ID типу підрозділу обов'язкове")
    private Long unitTypeId; // Посилання на UnitType (наприклад, ID типу "Рота")

    private Long parentUnitId; // Посилання на батька (наприклад, ID "1-го Батальйону")

    private String location;
    private LocalDate formationDate;
}