package com.example.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class BookingRequest {
    private LocalDate checkIn;
    private LocalDate checkOut;
    private Long roomId;
}
