package com.example.dto.request;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class RoomRequest {
    private String number;
    private String description;
    private int capacity;
    private boolean available;
    private int floor;
    private BigDecimal pricePerNight;
    private Long typeId;
}
