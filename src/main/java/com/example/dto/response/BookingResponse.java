package com.example.dto.response;

import com.example.enums.BookingStatus;
import lombok.Data;

import java.time.LocalDate;

@Data
public class BookingResponse {
    private Long id;
    private String username;
    private String roomNumber;
    private LocalDate checkIn;
    private LocalDate checkOut;
    private BookingStatus status;

    public BookingResponse(Long id, String username, String roomNumber,
                           LocalDate checkIn, LocalDate checkOut, BookingStatus status) {
        this.id = id;
        this.username = username;
        this.roomNumber = roomNumber;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.status = status;
    }
}