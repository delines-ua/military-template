package ua.edu.viti.military.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.edu.viti.military.entity.User;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // Знайти користувача для логіну
    Optional<User> findByUsername(String username);

    // Перевірки при реєстрації (щоб не було дублікатів)
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}