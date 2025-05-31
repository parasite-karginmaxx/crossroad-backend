package com.example.controller;

import com.example.dto.BookingRequest;
import com.example.model.Booking;
import com.example.model.User;
import com.example.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @PostMapping("/add")
    public ResponseEntity<?> createBooking(@RequestBody BookingRequest request, @AuthenticationPrincipal User user) {
        Booking booking = bookingService.createBooking(request, user);
        return ResponseEntity.ok(booking);
    }
}
