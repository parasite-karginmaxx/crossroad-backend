package com.example.controller.user;

import com.example.model.Addition;
import com.example.service.AdditionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Услуги (Пользователь)", description = "Эндпоинты услуг для пользователей")
@RestController
@RequestMapping("/api/additions")
@RequiredArgsConstructor
public class AdditionController {

    private final AdditionService additionService;

    @Operation(summary = "Получение всех дополнительных услуг")
    @GetMapping("/all")
    public ResponseEntity<List<Addition>> getAllAdditions() {
        return ResponseEntity.ok(additionService.getAllAdditions());
    }
}
