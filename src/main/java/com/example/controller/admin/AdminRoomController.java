package com.example.controller.admin;

import com.example.dto.request.RoomRequest;
import com.example.service.RoomService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/rooms")
@RequiredArgsConstructor
@Tag(name = "Комнаты (Админ)", description = "Эндпоинты работы с комнатами")
@PreAuthorize("hasRole('ADMIN')")
public class AdminRoomController {

    private final RoomService roomService;

    @Operation(summary = "Добавление комнаты")
    @PostMapping("/add")
    public ResponseEntity<String> createRoom(@RequestBody RoomRequest request) {
        return ResponseEntity.ok("Комната успешно добавлена\n" + roomService.createRoom(request));
    }

    @Operation(summary = "Обновление комнаты")
    @PutMapping("/{id}/update")
    public ResponseEntity<String> updateRoom(@PathVariable Long id, @RequestBody RoomRequest request) {
        return ResponseEntity.ok("Комната успешно обновлена\n" + roomService.updateRoom(id, request));
    }

    @Operation(summary = "Удаление комнаты")
    @DeleteMapping("/{id}/delete")
    public ResponseEntity<String> deleteRoom(@PathVariable Long id) {
        roomService.deleteRoom(id);
        return ResponseEntity.ok("Комната #" + id + " удалена");
    }
}
