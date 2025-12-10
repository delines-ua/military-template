package ua.edu.viti.military.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Future;
import lombok.Data;
import ua.edu.viti.military.entity.MedicalCategory;
import ua.edu.viti.military.entity.Rank;
import ua.edu.viti.military.entity.SecurityClearance;

import java.time.LocalDate;

@Data
public class PersonnelUpdateDTO {
    // Всі поля тут НЕ мають @NotBlank, бо вони опційні.
    // Якщо клієнт не передає поле, воно буде null, і ми його проігноруємо в сервісі.

    private String firstName;
    private String lastName;
    private String middleName;

    private Rank rank;
    private String specialization;

    private Long unitId; // Передати ID тільки якщо переводимо в інший підрозділ

    // Дата початку контракту зазвичай не змінюється, тому її тут немає.
    // А от продовжити контракт (змінити кінець) - можна.
    @Future(message = "Дата кінця контракту має бути в майбутньому")
    private LocalDate contractEndDate;

    private SecurityClearance securityClearance;
    private MedicalCategory medicalCategory;

    private String phoneNumber;

    @Email(message = "Некоректний формат email")
    private String email;
}