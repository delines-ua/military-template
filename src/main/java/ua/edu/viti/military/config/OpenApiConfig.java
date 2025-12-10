package ua.edu.viti.military.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Military Management System API",
                version = "1.0.0",
                description = "REST API для управління особовим складом та підрозділами військової частини",
                contact = @Contact(
                        name = "Курсант Науменко",
                        email = "student@viti.edu.ua"
                )
        ),
        servers = {
                @Server(
                        url = "http://localhost:8080",
                        description = "Local Development Server"
                )
        }
)
public class OpenApiConfig {
    // Цей клас просто містить анотації для налаштування Swagger
    // Spring знайде його автоматично завдяки @Configuration
}