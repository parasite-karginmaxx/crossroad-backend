package com.example.dto;

import lombok.Data;

@Data
public class RoomRequest {
    private String number;
    private int pricePerNight;
    private String description;
    private Long typeId;
}
