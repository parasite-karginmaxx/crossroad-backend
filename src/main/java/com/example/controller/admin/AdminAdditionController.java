package com.example.controller.admin;

import com.example.dto.request.AdditionRequest;
import com.example.model.Addition;
import com.example.service.AdditionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Услуги (Админ)", description = "Эндпоинты услуг для администраторов")
@RestController
@RequestMapping("/api/admin/additions")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminAdditionController {

    private final AdditionService additionService;

    @Operation(summary = "Добавить новую дополнительную услугу")
    @PostMapping("/add")
    public ResponseEntity<Addition> createAddition(@RequestBody AdditionRequest request) {
        Addition addition = additionService.createAddition(request);
        return ResponseEntity.ok(addition);
    }

    @Operation(summary = "Изменить текущую услугу")
    @PutMapping("/{id}/edit")
    public ResponseEntity<Addition> updateAddition(@PathVariable Long id, @RequestBody AdditionRequest request) {
        Addition updated = additionService.updateAddition(id, request);
        return ResponseEntity.ok(updated);
    }

    @Operation(summary = "Эндпоинт для удаления услуги")
    @DeleteMapping("/{id}/delete")
    public ResponseEntity<Void> deleteAddition(@PathVariable Long id) {
        additionService.deleteAddition(id);
        return ResponseEntity.noContent().build();
    }
}
