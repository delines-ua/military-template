package ua.edu.viti.military.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.edu.viti.military.entity.PersonnelMovement;

import java.util.List;

@Repository
public interface PersonnelMovementRepository extends JpaRepository<PersonnelMovement, Long> {

    // Отримати історію конкретного солдата (новіші зверху)
    List<PersonnelMovement> findByPersonnelIdOrderByPerformedAtDesc(Long personnelId);
}