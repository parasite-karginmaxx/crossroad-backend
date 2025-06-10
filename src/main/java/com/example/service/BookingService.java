package com.example.service;

import com.example.dto.request.BookingRequest;
import com.example.dto.response.BookingResponse;
import com.example.enums.BookingStatus;
import com.example.mapper.BookingMapper;
import com.example.model.*;
import com.example.repository.AdditionRepository;
import com.example.repository.BookingRepository;
import com.example.validator.BookingValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final BookingMapper bookingMapper;
    private final BookingValidator bookingValidator;
    private final BookingRepository bookingRepository;
    private final BookingStatusService statusService;
    private final AdditionRepository additionRepository;

    public Booking createBooking(BookingRequest request, User user) {
        bookingValidator.validateUserProfile(user);
        bookingValidator.validateDates(request.getCheckIn(), request.getCheckOut());

        Room room = bookingValidator.getRoomOrThrow(request.getRoomId());

        bookingValidator.checkUserAlreadyBookedThisRoom(user, room, request.getCheckIn(), request.getCheckOut());
        bookingValidator.checkRoomIsAvailable(room.getId(), request.getCheckIn(), request.getCheckOut());

        Booking booking = buildNewBooking(request, user, room);
        return bookingRepository.save(booking);
    }

    public List<BookingResponse> getUserBookings(User user) {
        return bookingRepository.findByUser(user).stream()
                .map(b -> bookingMapper.toResponse(b, false))
                .toList();
    }

    public List<BookingResponse> getAllBookingResponses() {
        return bookingRepository.findAll().stream()
                .map(b -> bookingMapper.toResponse(b, true))
                .toList();
    }

    public Booking getBookingById(Long id) {
        return bookingRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Бронирование с ID " + id + " не найдено"));
    }

    @Transactional
    public Booking updateBooking(Long id, BookingRequest request, User user) {
        Booking booking = getBookingById(id);
        bookingValidator.checkOwner(booking, user);

        BookingStatus status = booking.getStatus();

        if (bookingValidator.isFinalStatus(status)) {
            throw new IllegalStateException("Нельзя редактировать бронирование со статусом: " + status);
        }

        if (status == BookingStatus.ACTIVE) {
            statusService.handleActiveBookingExtension(booking, request);
        } else if (status == BookingStatus.CONFIRMED) {
            statusService.handleConfirmedBookingModification(booking, request);
        } else {
            bookingValidator.validateDates(request.getCheckIn(), request.getCheckOut());
            booking.setCheckIn(request.getCheckIn());
            booking.setCheckOut(request.getCheckOut());
        }

        return bookingRepository.save(booking);
    }

    @Transactional
    public void cancelBookingByUserOrThrow(Long id, User user) {
        Booking booking = getBookingById(id);
        bookingValidator.checkOwner(booking, user);

        if (!List.of(BookingStatus.PENDING, BookingStatus.CONFIRMED).contains(booking.getStatus())) {
            throw new IllegalStateException("Нельзя отменить бронирование со статусом: " + booking.getStatus());
        }

        booking.setStatus(BookingStatus.CANCELLED);
        bookingRepository.save(booking);
    }

    public void deleteBooking(Long id) {
        if (!bookingRepository.existsById(id)) {
            throw new RuntimeException("Бронирование не найдено");
        }
        bookingRepository.deleteById(id);
    }

    /**
     *  Вспомогательные методы
     */

    private Booking buildNewBooking(BookingRequest request, User user, Room room) {
        Booking booking = Booking.builder()
                .checkIn(request.getCheckIn())
                .checkOut(request.getCheckOut())
                .originalCheckOut(null)
                .room(room)
                .user(user)
                .status(BookingStatus.PENDING)
                .build();

        if (request.getAdditionIds() != null && !request.getAdditionIds().isEmpty()) {
            List<Addition> additions = additionRepository.findAllById(request.getAdditionIds());

            List<BookingAddition> bookingAdditions = additions.stream()
                    .map(addition -> BookingAddition.builder()
                            .name(addition.getName())
                            .price(addition.getPrice())
                            .booking(booking)
                            .build())
                    .toList();

            booking.setAdditions(bookingAdditions);
        }

        return booking;
    }
}
