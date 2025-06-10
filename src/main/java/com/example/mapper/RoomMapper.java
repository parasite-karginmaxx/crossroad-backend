package com.example.mapper;

import com.example.dto.response.RoomResponse;
import com.example.model.Room;
import org.springframework.stereotype.Component;

@Component
public class RoomMapper {

    public RoomResponse toResponse(Room room) {
        return RoomResponse.builder()
                .id(room.getId())
                .number(room.getNumber())
                .description(room.getDescription())
                .floor(room.getFloor())
                .capacity(room.getCapacity())
                .pricePerNight(room.getPricePerNight())
                .typeName(room.getRoomType() != null ? room.getRoomType().getName() : null)
                .build();
    }
}
