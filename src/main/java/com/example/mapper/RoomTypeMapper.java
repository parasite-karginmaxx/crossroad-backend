package com.example.mapper;

import com.example.dto.response.RoomTypeResponse;
import com.example.model.RoomType;
import org.springframework.stereotype.Component;

@Component
public class RoomTypeMapper {

    public RoomTypeResponse toResponse(RoomType type) {
        return RoomTypeResponse.builder()
                .id(type.getId())
                .name(type.getName())
                .description(type.getDescription())
                .build();
    }
}
