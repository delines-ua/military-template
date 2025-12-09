package ua.edu.viti.military.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.edu.viti.military.entity.Personnel;
import java.util.List;
import java.util.Optional;

@Repository
public interface PersonnelRepository extends JpaRepository<Personnel, Long> {
    Optional<Personnel> findByMilitaryId(String militaryId);
    boolean existsByMilitaryId(String militaryId);
    List<Personnel> findByUnitId(Long unitId);
}