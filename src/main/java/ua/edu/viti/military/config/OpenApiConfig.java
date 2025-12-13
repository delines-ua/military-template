package ua.edu.viti.military.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Military Management System API",
                version = "2.0.0",
                description = "API для управління військовим обліком",
                contact = @Contact(
                        name = "Support",
                        email = "admin@mil.gov.ua"
                )
        ),
        servers = {
                @Server(url = "http://localhost:8080", description = "Local Environment")
        },
        // 🔥 Цей рядок каже Swagger'у додавати токен до ВСІХ запитів
        security = @SecurityRequirement(name = "bearerAuth")
)
@SecurityScheme(
        name = "bearerAuth", // Це ім'я має співпадати з тим, що в security вище
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT",
        description = "Введіть JWT токен (без слова Bearer)"
)
public class OpenApiConfig {
}