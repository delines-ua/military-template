package ua.edu.viti.military.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "personnel_movements")
@EntityListeners(AuditingEntityListener.class)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PersonnelMovement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "personnel_id", nullable = false)
    private Personnel personnel;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private MovementType type;

    // Звідки (може бути null, якщо це ENLISTMENT)
    @Column(name = "from_unit_name")
    private String fromUnitName;

    // Куди (може бути null, якщо це DISMISSAL)
    @Column(name = "to_unit_name")
    private String toUnitName;

    // Примітки (наказ №...)
    @Column(length = 500)
    private String notes;

    // Хто виконав операцію (поки що просто рядок, у Session 2 підключимо User)
    @Column(name = "performed_by")
    private String performedBy;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime performedAt;

    // Optimistic Locking (запобігає конфліктам)
    @Version
    private Long version;
}