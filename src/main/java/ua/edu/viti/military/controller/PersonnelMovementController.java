package ua.edu.viti.military.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.edu.viti.military.dto.request.MovementRequestDTO;
import ua.edu.viti.military.dto.response.MovementResponseDTO;
import ua.edu.viti.military.service.PersonnelMovementService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/movements")
@RequiredArgsConstructor
@Tag(name = "Personnel Movements", description = "Управління переміщеннями особового складу")
public class PersonnelMovementController {

    private final PersonnelMovementService movementService;

    @PostMapping("/transfer")
    @Operation(summary = "Перевести військовослужбовця в інший підрозділ")
    public ResponseEntity<MovementResponseDTO> transfer(@RequestBody @Valid MovementRequestDTO dto) {
        return ResponseEntity.ok(movementService.transferPersonnel(dto));
    }

    @GetMapping("/history/{personnelId}")
    @Operation(summary = "Отримати історію переміщень солдата")
    public ResponseEntity<List<MovementResponseDTO>> getHistory(@PathVariable Long personnelId) {
        return ResponseEntity.ok(movementService.getHistory(personnelId));
    }
}