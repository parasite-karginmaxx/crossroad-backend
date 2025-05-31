package com.example.service;

import com.example.dto.BookingRequest;
import com.example.model.Booking;
import com.example.model.Room;
import com.example.model.User;
import com.example.repository.BookingRepository;
import com.example.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final RoomRepository roomRepository;
    private final BookingRepository bookingRepository;

    public Booking createBooking(BookingRequest request, User user) {
        Room room = roomRepository.findById(request.getRoomId())
                .orElseThrow(() -> new RuntimeException("Room not found"));

        Booking booking = Booking.builder()
                .checkIn(request.getCheckIn())
                .checkOut(request.getCheckOut())
                .room(room)
                .user(user)
                .status(false) // По умолчанию не подтверждено
                .build();

        return bookingRepository.save(booking);
    }
}
