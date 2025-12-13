package ua.edu.viti.military.dto.response;

import lombok.Data;
import ua.edu.viti.military.entity.MedicalCategory;
import ua.edu.viti.military.entity.Rank;
import ua.edu.viti.military.entity.SecurityClearance;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class PersonnelResponseDTO implements Serializable {
    private Long id;
    private String militaryId;
    private String fullName; // Тут буде "Прізвище Ім'я По-батькові" одним рядком
    private Rank rank;
    private String specialization;
    private static final long serialVersionUID = 1L;
    private String unitName; // Назва підрозділу, щоб не показувати сухий ID

    private LocalDate contractEndDate;
    private SecurityClearance securityClearance;

    private LocalDateTime createdAt;
}