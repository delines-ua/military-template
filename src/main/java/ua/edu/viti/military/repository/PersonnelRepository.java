package ua.edu.viti.military.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.edu.viti.military.entity.Personnel;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ua.edu.viti.military.entity.Rank;
import java.util.List;
import java.util.Optional;

@Repository
public interface PersonnelRepository extends JpaRepository<Personnel, Long> {
    Optional<Personnel> findByMilitaryId(String militaryId);
    boolean existsByMilitaryId(String militaryId);
    List<Personnel> findByUnitId(Long unitId);

    @Query("SELECT p FROM Personnel p WHERE p.rank = :rank AND p.unit.id = :unitId")
    List<Personnel> findByRankAndUnit(@Param("rank") Rank rank, @Param("unitId") Long unitId);
}