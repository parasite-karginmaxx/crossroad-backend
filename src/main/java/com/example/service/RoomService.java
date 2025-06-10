package com.example.service;

import com.example.dto.request.RoomRequest;
import com.example.model.Room;
import com.example.model.RoomType;
import com.example.repository.RoomRepository;
import com.example.repository.RoomTypeRepository;
import com.example.validator.RoomValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class RoomService {

    private final RoomRepository roomRepository;
    private final RoomTypeRepository roomTypeRepository;
    private final RoomValidator roomValidator;

    public Room createRoom(RoomRequest request) {
        String number = request.getNumber();
        String validation = roomValidator.validateRoomNumber(number);
        if (validation != null) throw new IllegalArgumentException(validation);

        if (roomRepository.existsByNumber(number)) {
            throw new IllegalArgumentException("Комната с таким номером уже существует");
        }

        RoomType roomType = getTypeById(request.getTypeId());
        if (roomType == null) throw new NoSuchElementException("Тип комнаты не найден");

        Room room = buildRoom(request, roomType);
        return roomRepository.save(room);
    }

    public Room updateRoom(Long id, RoomRequest request) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Комната не найдена"));

        RoomType roomType = getTypeById(request.getTypeId());
        if (roomType == null) throw new NoSuchElementException("Тип комнаты не найден");

        room.setDescription(request.getDescription());
        room.setCapacity(request.getCapacity());
        room.setFloor(request.getFloor());
        room.setPricePerNight(request.getPricePerNight());
        room.setRoomType(roomType);

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

    private RoomType getTypeById(Long id) {
        return roomTypeRepository.findById(id).orElse(null);
    }

    private Room buildRoom(RoomRequest request, RoomType roomType) {
        return Room.builder()
                .number(request.getNumber())
                .description(request.getDescription())
                .capacity(request.getCapacity())
                .floor(request.getFloor())
                .pricePerNight(request.getPricePerNight())
                .roomType(roomType)
                .build();
    }
}
