package com.example.service;

import com.example.dto.request.RoomRequest;
import com.example.model.Room;
import com.example.model.Type;
import com.example.repository.RoomRepository;
import com.example.repository.TypeRepository;
import com.example.validator.RoomValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class RoomService {

    private final RoomRepository roomRepository;
    private final TypeRepository typeRepository;
    private final RoomValidator roomValidator;

    public Room createRoom(RoomRequest request) {
        String number = request.getNumber();
        String validation = roomValidator.validateRoomNumber(number);
        if (validation != null) throw new IllegalArgumentException(validation);

        if (roomRepository.existsByNumber(number)) {
            throw new IllegalArgumentException("Комната с таким номером уже существует");
        }

        Type type = getTypeById(request.getTypeId());
        if (type == null) throw new NoSuchElementException("Тип комнаты не найден");

        Room room = buildRoom(request, type);
        return roomRepository.save(room);
    }

    public Room updateRoom(Long id, RoomRequest request) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Комната не найдена"));

        Type type = getTypeById(request.getTypeId());
        if (type == null) throw new NoSuchElementException("Тип комнаты не найден");

        room.setPricePerNight(request.getPricePerNight());
        room.setDescription(request.getDescription());
        room.setType(type);

        return roomRepository.save(room);
    }

    public void deleteRoom(Long id) {
        if (!roomRepository.existsById(id)) {
            throw new RuntimeException("Комната не найдено");
        }

        roomRepository.deleteById(id);
    }

    public List<Room> getAllRooms() {
        return roomRepository.findAll();
    }

    public Room getRoomByIdOrThrow(Long id) {
        return roomRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Комната не найдена"));
    }

    /**
     *  Вспомогательные методы
     */

    private Type getTypeById(Long id) {
        return typeRepository.findById(id).orElse(null);
    }

    private Room buildRoom(RoomRequest request, Type type) {
        return Room.builder()
                .number(request.getNumber())
                .pricePerNight(request.getPricePerNight())
                .description(request.getDescription())
                .type(type)
                .build();
    }
}
