package com.example.service;

import com.example.dto.BookingRequest;
import com.example.model.Booking;
import com.example.model.Room;
import com.example.model.User;
import com.example.repository.BookingRepository;
import com.example.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    public Booking getBookingById(Long id) {
        return bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Бронирование с ID " + id + " не найдено"));
    }

    public Booking updateBooking(Long id, BookingRequest request) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Бронирование не найдено"));

        booking.setCheckIn(request.getCheckIn());
        booking.setCheckOut(request.getCheckOut());

        if (!booking.getRoom().getId().equals(request.getRoomId())) {
            Room room = roomRepository.findById(request.getRoomId())
                    .orElseThrow(() -> new RuntimeException("Комната не найдена"));
            booking.setRoom(room);
        }

        return bookingRepository.save(booking);
    }

    public void deleteBooking(Long id) {
        if (!bookingRepository.existsById(id)) {
            throw new RuntimeException("Бронирование не найдено");
        }
        bookingRepository.deleteById(id);
    }
}
