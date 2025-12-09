package ua.edu.viti.military.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.edu.viti.military.dto.request.PersonnelCreateDTO;
import ua.edu.viti.military.dto.response.PersonnelResponseDTO;
import ua.edu.viti.military.service.PersonnelService;

import java.util.List;

@RestController // Каже Spring, що це контролер, який обробляє запити
@RequestMapping("/api/v1/personnel") // Базова адреса для всіх методів тут
@RequiredArgsConstructor
@Tag(name = "Personnel API", description = "Управління особовим складом") // Для красивого Swagger
public class PersonnelController {

    private final PersonnelService personnelService;

    // 1. Створити нового військового
    // POST http://localhost:8080/api/v1/personnel
    @PostMapping
    @Operation(summary = "Створити картку військовослужбовця")
    public ResponseEntity<PersonnelResponseDTO> create(@RequestBody @Valid PersonnelCreateDTO dto) {
        // @Valid перевіряє анотації в DTO (Not Blank, Future date і т.д.)
        PersonnelResponseDTO created = personnelService.create(dto);
        return new ResponseEntity<>(created, HttpStatus.CREATED); // Повертає статус 201 Created
    }

    // 2. Отримати по ID
    // GET http://localhost:8080/api/v1/personnel/{id}
    @GetMapping("/{id}")
    @Operation(summary = "Знайти військового за ID")
    public ResponseEntity<PersonnelResponseDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(personnelService.getById(id));
    }

    // 3. Отримати всіх
    // GET http://localhost:8080/api/v1/personnel
    @GetMapping
    @Operation(summary = "Отримати список всього персоналу")
    public ResponseEntity<List<PersonnelResponseDTO>> getAll() {
        return ResponseEntity.ok(personnelService.getAll());
    }
}