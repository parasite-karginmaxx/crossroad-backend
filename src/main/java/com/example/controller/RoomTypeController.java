package com.example.controller;

import com.example.dto.request.TypeRequest;
import com.example.model.RoomType;
import com.example.service.RoomTypeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Тип комнаты", description = "Эндпоинты для типов комнат")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/types")
public class RoomTypeController {

    private final RoomTypeService roomTypeService;

    @Operation(summary = "Добавление типа комнаты")
    @PostMapping("/add")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RoomType> createType(@RequestBody TypeRequest request) {
        RoomType created = roomTypeService.createType(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @Operation(summary = "Получение всех типов комнат")
    @GetMapping("/all")
    public ResponseEntity<List<RoomType>> getAllTypes() {
        return ResponseEntity.ok(roomTypeService.getAllTypes());
    }

    @Operation(summary = "Изменение текущего типа комнаты")
    @PutMapping("/{id}/edit")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RoomType> updateType(@PathVariable Long id, @RequestBody TypeRequest request) {
        RoomType updated = roomTypeService.updateType(id, request);
        return ResponseEntity.ok(updated);
    }

    @Operation(summary = "Удаление типа комнаты")
    @DeleteMapping("/{id}/delete")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteType(@PathVariable Long id) {
        roomTypeService.deleteType(id);
        return ResponseEntity.noContent().build();
    }
}
