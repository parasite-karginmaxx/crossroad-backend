package com.example.mapper;

import com.example.dto.response.BookingResponse;
import com.example.model.Booking;
import com.example.model.User;
import org.springframework.stereotype.Component;

@Component
public class BookingMapper {

    public BookingResponse toResponse(Booking booking, boolean isAdminView) {
        User user = booking.getUser();
        var profile = user.getProfile();

        String fullName = profile != null
                ? String.join(" ",
                safe(profile.getLastName()),
                safe(profile.getFirstName()),
                safe(profile.getMiddleName())).trim()
                : "";

        return BookingResponse.builder()
                .id(booking.getId())
                .username(user.getUsername())
                .fullName(isAdminView && profile != null ? fullName : null)
                .phone(isAdminView && profile != null ? profile.getPhone() : null)
                .passport(isAdminView && profile != null ? profile.getPassportNumber() : null)
                .citizenship(isAdminView && profile != null ? profile.getCitizenship() : null)
                .roomNumber(booking.getRoom().getNumber())
                .checkIn(booking.getCheckIn())
                .checkOut(booking.getCheckOut())
                .status(booking.getStatus())
                .build();
    }

    private String safe(String s) {
        return s == null ? "" : s;
    }
}
