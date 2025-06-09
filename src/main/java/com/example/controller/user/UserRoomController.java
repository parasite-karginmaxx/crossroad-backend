package com.example.controller.user;

import com.example.model.Room;
import com.example.service.RoomService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rooms")
@RequiredArgsConstructor
@Tag(name = "Комнаты (Пользователи)", description = "Эндпоинты получения комнат")
public class UserRoomController {

    private final RoomService roomService;

    @Operation(summary = "Получение всех комнат")
    @GetMapping("/all")
    public ResponseEntity<List<Room>> getAllRooms() {
        return ResponseEntity.ok(roomService.getAllRooms());
    }

    @Operation(summary = "Получение комнаты по ID")
    @GetMapping("/{id}")
    public ResponseEntity<Room> getRoomById(@PathVariable Long id) {
        return ResponseEntity.ok(roomService.getRoomByIdOrThrow(id));
    }
}
