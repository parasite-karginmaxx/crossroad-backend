package com.example.service;

import com.example.dto.request.BookingRequest;
import com.example.dto.response.BookingResponse;
import com.example.enums.BookingStatus;
import com.example.model.Booking;
import com.example.model.Room;
import com.example.model.User;
import com.example.repository.BookingRepository;
import com.example.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final RoomRepository roomRepository;
    private final BookingRepository bookingRepository;

    public Booking createBooking(BookingRequest request, User user) {
        validateDates(request.getCheckIn(), request.getCheckOut());

        Room room = roomRepository.findById(request.getRoomId())
                .orElseThrow(() -> new RuntimeException("Комната не найдена"));

        Booking booking = Booking.builder()
                .checkIn(request.getCheckIn())
                .checkOut(request.getCheckOut())
                .originalCheckOut(request.getCheckOut())
                .room(room)
                .user(user)
                .status(BookingStatus.PENDING)
                .build();

        return bookingRepository.save(booking);
    }

    public List<BookingResponse> getUserBookings(User user) {
        return bookingRepository.findAllByUser(user).stream()
                .map(b -> mapToResponse(b, user.getUsername()))
                .toList();
    }

    public List<BookingResponse> getAllBookingResponses() {
        return bookingRepository.findAll().stream()
                .map(b -> mapToResponse(b, b.getUser().getUsername()))
                .toList();
    }

    public Booking getBookingById(Long id) {
        return bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Бронирование с ID " + id + " не найдено"));
    }

    @Transactional
    public Booking updateBooking(Long id, BookingRequest request, User user) {
        Booking booking = getBookingById(id);
        checkOwner(booking, user);

        BookingStatus status = booking.getStatus();

        if (isFinalStatus(status)) {
            throw new IllegalStateException("Нельзя редактировать бронирование со статусом: " + status);
        }

        if (status == BookingStatus.ACTIVE) {
            handleActiveBookingExtension(booking, request);
        } else {
            validateDates(request.getCheckIn(), request.getCheckOut());
            booking.setCheckIn(request.getCheckIn());
            booking.setCheckOut(request.getCheckOut());
        }

        return bookingRepository.save(booking);
    }

    @Transactional
    public void updateBookingStatusByAdmin(Long id, BookingStatus newStatus) {
        Booking booking = getBookingById(id);

        if (isFinalStatus(booking.getStatus())) {
            throw new IllegalStateException("Нельзя изменить статус у завершенного или отмененного бронирования");
        }

        booking.setStatus(newStatus);
        bookingRepository.save(booking);
    }

    @Transactional
    public void cancelBookingByUserOrThrow(Long id, User user) {
        Booking booking = getBookingById(id);
        checkOwner(booking, user);

        if (!List.of(BookingStatus.PENDING, BookingStatus.CONFIRMED).contains(booking.getStatus())) {
            throw new IllegalStateException("Нельзя отменить бронирование со статусом: " + booking.getStatus());
        }

        booking.setStatus(BookingStatus.CANCELLED);
        bookingRepository.save(booking);
    }

    @Transactional
    public void approveBookingExtension(Long id) {
        Booking booking = getBookingById(id);

        if (booking.getStatus() != BookingStatus.EXTENSION_REQUESTED) {
            throw new IllegalStateException("Продление не запрошено для этого бронирования");
        }

        booking.setStatus(BookingStatus.ACTIVE);
        bookingRepository.save(booking);
    }

    @Transactional
    public void rejectBookingExtension(Long id) {
        Booking booking = getBookingById(id);

        if (booking.getStatus() != BookingStatus.EXTENSION_REQUESTED) {
            throw new IllegalStateException("Продление не запрошено для этого бронирования");
        }

        booking.setCheckOut(booking.getOriginalCheckOut());
        booking.setStatus(BookingStatus.ACTIVE);
        bookingRepository.save(booking);
    }

    public void deleteBooking(Long id) {
        if (!bookingRepository.existsById(id)) {
            throw new RuntimeException("Бронирование не найдено");
        }
        bookingRepository.deleteById(id);
    }

    // Вспомогательные методы

    private BookingResponse mapToResponse(Booking booking, String username) {
        return new BookingResponse(
                booking.getId(),
                username,
                booking.getRoom().getNumber(),
                booking.getCheckIn(),
                booking.getCheckOut(),
                booking.getStatus()
        );
    }

    private void validateDates(LocalDate checkIn, LocalDate checkOut) {
        if (checkIn.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Дата заезда не может быть в прошлом");
        }
        if (checkOut.isBefore(checkIn)) {
            throw new IllegalArgumentException("Дата выезда должна быть позже даты заезда");
        }
    }

    private void checkOwner(Booking booking, User user) {
        if (!booking.getUser().getId().equals(user.getId())) {
            throw new AccessDeniedException("Вы не можете редактировать чужое бронирование");
        }
    }

    private boolean isFinalStatus(BookingStatus status) {
        return List.of(BookingStatus.CANCELLED, BookingStatus.REJECTED, BookingStatus.COMPLETED).contains(status);
    }

    private void handleActiveBookingExtension(Booking booking, BookingRequest request) {
        if (!request.getCheckIn().isEqual(booking.getCheckIn())) {
            throw new IllegalStateException("Нельзя изменять дату заезда для активного бронирования");
        }

        if (request.getCheckOut().isAfter(booking.getCheckOut())) {
            booking.setOriginalCheckOut(booking.getCheckOut());
            booking.setCheckOut(request.getCheckOut());
            booking.setStatus(BookingStatus.EXTENSION_REQUESTED);
        } else {
            throw new IllegalStateException("Нельзя сократить период активного проживания");
        }
    }
}
