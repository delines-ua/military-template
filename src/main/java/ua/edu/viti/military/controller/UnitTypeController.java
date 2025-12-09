package ua.edu.viti.military.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.edu.viti.military.dto.request.UnitTypeCreateDTO;
import ua.edu.viti.military.dto.response.UnitTypeResponseDTO;
import ua.edu.viti.military.service.UnitTypeService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/unit-types")
@RequiredArgsConstructor
@Tag(name = "Unit Type API", description = "Довідник типів підрозділів")
public class UnitTypeController {

    private final UnitTypeService unitTypeService;

    @PostMapping
    @Operation(summary = "Створити новий тип підрозділу")
    public ResponseEntity<UnitTypeResponseDTO> create(@RequestBody @Valid UnitTypeCreateDTO dto) {
        return new ResponseEntity<>(unitTypeService.create(dto), HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "Отримати всі типи")
    public ResponseEntity<List<UnitTypeResponseDTO>> getAll() {
        return ResponseEntity.ok(unitTypeService.getAll());
    }
}