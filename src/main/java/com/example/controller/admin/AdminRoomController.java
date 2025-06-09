package com.example.controller.admin;

import com.example.dto.request.RoomRequest;
import com.example.model.Room;
import com.example.model.Type;
import com.example.repository.RoomRepository;
import com.example.repository.TypeRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/rooms")
@RequiredArgsConstructor
@Tag(name = "Комнаты (Админ)", description = "Эндпоинты работы с комнатами")
@PreAuthorize("hasRole('ADMIN')")
public class AdminRoomController {

    private final RoomRepository roomRepository;
    private final TypeRepository typeRepository;

    @Operation(summary = "Добавление комнаты")
    @PostMapping("/add")
    public ResponseEntity<String> createRoom(@RequestBody RoomRequest request) {
        if (roomRepository.existsByNumber(request.getNumber())) {
            return ResponseEntity.badRequest().body("Комната с таким номером уже существует");
        }

        Type type = typeRepository.findById(request.getTypeId()).orElse(null);
        if (type == null) {
            return ResponseEntity.badRequest().body("Тип комнаты не найден");
        }

        Room room = Room.builder()
                .number(request.getNumber())
                .pricePerNight(request.getPricePerNight())
                .description(request.getDescription())
                .type(type)
                .build();

        roomRepository.save(room);
        return ResponseEntity.ok("Комната успешно добавлена");
    }

    @Operation(summary = "Удаление комнаты")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteRoom(@PathVariable Long id) {
        if (!roomRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        roomRepository.deleteById(id);
        return ResponseEntity.ok("Комната успешно удалена");
    }
}
