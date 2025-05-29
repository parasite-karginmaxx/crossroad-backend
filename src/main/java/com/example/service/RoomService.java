package com.example.service;

import com.example.dto.RoomRequest;
import com.example.model.Room;
import com.example.model.Type;
import com.example.repository.RoomRepository;
import com.example.repository.TypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoomService {

    private final RoomRepository roomRepository;
    private final TypeRepository typeRepository;

    public String createRoom(RoomRequest request) {
        if (roomRepository.existsByNumber(request.getNumber())) {
            return "Комната с таким номером уже существует";
        }

        Type type = typeRepository.findById(request.getTypeId()).orElse(null);
        if (type == null) {
            return "Тип комнаты не найден";
        }

        Room room = Room.builder()
                .number(request.getNumber())
                .pricePerNight(request.getPricePerNight())
                .description(request.getDescription())
                .type(type)
                .build();

        roomRepository.save(room);
        return "Комната успешно добавлена";
    }

    public String deleteRoom(Long id) {
        if (!roomRepository.existsById(id)) {
            return "Комната не найдена";
        }

        roomRepository.deleteById(id);
        return "Комната успешно удалена";
    }

    public List<Room> getAllRooms() {
        return roomRepository.findAll();
    }
}

