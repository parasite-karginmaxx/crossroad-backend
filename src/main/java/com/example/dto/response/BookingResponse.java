package com.example.dto.response;

import com.example.enums.BookingStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.Builder;

import java.time.LocalDate;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@Builder
public class BookingResponse {
    private Long id;
    private String username;
    private String fullName;
    private String phone;
    private String passport;
    private String citizenship;
    private String roomNumber;
    private LocalDate checkIn;
    private LocalDate checkOut;
    private BookingStatus status;

    public BookingResponse(Long id, String username,
                           String fullName, String phone, String passport, String citizenship,
                           String roomNumber, LocalDate checkIn, LocalDate checkOut, BookingStatus status){
        this.id = id;
        this.username = username;
        this.fullName = fullName;
        this.phone = phone;
        this.passport = passport;
        this.citizenship = citizenship;
        this.roomNumber = roomNumber;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.status = status;
    }
}