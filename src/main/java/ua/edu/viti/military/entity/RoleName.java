package ua.edu.viti.military.entity;

public enum RoleName {
    ROLE_ADMIN,      // Адмін: Може все (звільняти, видаляти)
    ROLE_COMMANDER,  // Командир: Може переводити, але не може видаляти історію
    ROLE_USER        // Звичайний доступ: Тільки перегляд
}