package ua.edu.viti.military.dto.request;

import lombok.Data;
import java.time.LocalDate;

@Data
public class MilitaryUnitUpdateDTO {
    // Всі поля опційні. Якщо поле null - ми його не змінюємо.

    private String name;
    private String code; // Можна змінити код (наприклад, виправити помилку)
    private String location;

    private Long unitTypeId; // Можна змінити тип (наприклад, Взвод став Ротою)
    private Long parentUnitId; // Можна змінити підпорядкування (перевести в інший батальйон)

    private LocalDate formationDate;
}