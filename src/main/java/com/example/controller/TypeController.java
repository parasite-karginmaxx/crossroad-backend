package com.example.controller;

import com.example.dto.RoomRequest;
import com.example.model.Room;
import com.example.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/types")
public class TypeController {

    private final RoomService roomService;

    @PostMapping("/add")
    public ResponseEntity<String> createRoom(@RequestBody RoomRequest request) {
        String result = roomService.createRoom(request);
        if (result.contains("существует") || result.contains("не найден")) {
            return ResponseEntity.badRequest().body(result);
        }
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteRoom(@PathVariable Long id) {
        String result = roomService.deleteRoom(id);
        if (result.contains("не найдена")) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(result);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Room>> getAllRooms() {
        return ResponseEntity.ok(roomService.getAllRooms());
    }
}
