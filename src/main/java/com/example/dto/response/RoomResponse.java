package com.example.dto.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class RoomResponse {
    private Long id;
    private String number;
    private String description;
    private int floor;
    private int capacity;
    private BigDecimal pricePerNight;
    private String typeName;
}
