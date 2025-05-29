package com.example.controller;

import com.example.dto.RoomRequest;
import com.example.model.Room;
import com.example.model.Type;
import com.example.repository.RoomRepository;
import com.example.repository.TypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rooms")
@RequiredArgsConstructor
public class RoomController {

    private final RoomRepository roomRepository;
    private final TypeRepository typeRepository;

    // 🔹 Получить все комнаты
    @GetMapping("/all")
    public ResponseEntity<List<Room>> getAllRooms() {
        return ResponseEntity.ok(roomRepository.findAll());
    }

    // 🔹 Добавить комнату
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

    // 🔹 Удалить комнату
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteRoom(@PathVariable Long id) {
        if (!roomRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        roomRepository.deleteById(id);
        return ResponseEntity.ok("Комната успешно удалена");
    }
}
