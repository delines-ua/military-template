package ua.edu.viti.military.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.edu.viti.military.dto.request.PersonnelCreateDTO;
import ua.edu.viti.military.dto.response.ErrorResponseDTO;
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

    // 3. Отримати всіх3
    // GET http://localhost:8080/api/v1/personnel
    @GetMapping
    @Operation(summary = "Отримати список всього персоналу")
    public ResponseEntity<List<PersonnelResponseDTO>> getAll() {
        return ResponseEntity.ok(personnelService.getAll());
    }
    @PutMapping("/{id}")
    @Operation(summary = "Оновити дані військовослужбовця")
    public ResponseEntity<PersonnelResponseDTO> update(
            @PathVariable Long id,
            @RequestBody @Valid ua.edu.viti.military.dto.request.PersonnelUpdateDTO dto) {
        return ResponseEntity.ok(personnelService.update(id, dto));
    }
    @DeleteMapping("/{id}")
    @Operation(summary = "Видалити картку військового")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        personnelService.delete(id);
        return ResponseEntity.noContent().build(); // Повертає статус 204 No Content (успіх без тіла)
    }
    @GetMapping("/filter")
    @Operation(summary = "Знайти військових за званням у конкретному підрозділі (Custom JPQL Query)")
    public ResponseEntity<List<PersonnelResponseDTO>> filterByRankAndUnit(
            @RequestParam ua.edu.viti.military.entity.Rank rank,
            @RequestParam Long unitId) {
        return ResponseEntity.ok(personnelService.getByRankAndUnit(rank, unitId));
    }
    @GetMapping("/{id}")
    @Operation(summary = "Отримати інформацію про військового за ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Військового знайдено"),
            @ApiResponse(responseCode = "404", description = "Військового не знайдено",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "Не авторизований (потрібен токен)",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    public ResponseEntity<PersonnelResponseDTO> getPersonnelById(@PathVariable Long id) {
        return ResponseEntity.ok(personnelService.getById(id));
        }
}