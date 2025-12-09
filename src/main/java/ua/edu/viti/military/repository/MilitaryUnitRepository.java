package ua.edu.viti.military.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.edu.viti.military.entity.MilitaryUnit;
import java.util.List;
import java.util.Optional;

@Repository
public interface MilitaryUnitRepository extends JpaRepository<MilitaryUnit, Long> {
    Optional<MilitaryUnit> findByCode(String code);
    List<MilitaryUnit> findByUnitTypeId(Long unitTypeId);
}