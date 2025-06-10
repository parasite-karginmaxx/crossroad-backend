package com.example.dto.request;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class BookingRequest {
    private LocalDate checkIn;
    private LocalDate checkOut;
    private Long roomId;
    List<Long> additionIds;
}
