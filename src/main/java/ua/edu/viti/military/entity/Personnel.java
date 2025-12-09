package ua.edu.viti.military.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "personnel")
@EntityListeners(AuditingEntityListener.class)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Personnel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String militaryId; // Військовий квиток / ID

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    private String middleName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Rank rank; // Звання

    @Column(nullable = false)
    private String specialization; // ВОС

    @ManyToOne
    @JoinColumn(name = "unit_id")
    private MilitaryUnit unit; // Прив'язка до підрозділу

    @Column(nullable = false)
    private LocalDate contractStartDate;

    @Column(nullable = false)
    private LocalDate contractEndDate;

    @Enumerated(EnumType.STRING)
    private SecurityClearance securityClearance;

    @Enumerated(EnumType.STRING)
    private MedicalCategory medicalCategory;

    private String phoneNumber;
    private String email;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;
}