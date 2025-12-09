package ua.edu.viti.military.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import ua.edu.viti.military.entity.MedicalCategory;
import ua.edu.viti.military.entity.Rank;
import ua.edu.viti.military.entity.SecurityClearance;

import java.time.LocalDate;

@Data
public class PersonnelCreateDTO {

    @NotBlank(message = "Військовий ID обов'язковий")
    private String militaryId;

    @NotBlank(message = "Ім'я обов'язкове")
    private String firstName;

    @NotBlank(message = "Прізвище обов'язкове")
    private String lastName;

    private String middleName;

    @NotNull(message = "Звання обов'язкове")
    private Rank rank;

    @NotBlank(message = "ВОС обов'язкова")
    private String specialization;

    private Long unitId; // Ми передаємо тільки ID підрозділу

    @NotNull
    private LocalDate contractStartDate;

    @NotNull
    private LocalDate contractEndDate;

    private SecurityClearance securityClearance;
    private MedicalCategory medicalCategory;

    private String phoneNumber;
    private String email;
}