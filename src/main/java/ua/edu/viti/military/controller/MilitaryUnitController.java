package ua.edu.viti.military.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.edu.viti.military.dto.request.MilitaryUnitCreateDTO;
import ua.edu.viti.military.dto.response.MilitaryUnitResponseDTO;
import ua.edu.viti.military.service.MilitaryUnitService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/units")
@RequiredArgsConstructor
@Tag(name = "Military Unit API", description = "Управління підрозділами")
public class MilitaryUnitController {

    private final MilitaryUnitService militaryUnitService;

    @PostMapping
    @Operation(summary = "Створити новий підрозділ")
    public ResponseEntity<MilitaryUnitResponseDTO> create(@RequestBody @Valid MilitaryUnitCreateDTO dto) {
        return new ResponseEntity<>(militaryUnitService.create(dto), HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "Отримати структуру підрозділів")
    public ResponseEntity<List<MilitaryUnitResponseDTO>> getAll() {
        return ResponseEntity.ok(militaryUnitService.getAll());
    }
}