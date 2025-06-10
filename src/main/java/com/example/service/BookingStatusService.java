package com.example.service;

import com.example.dto.request.BookingRequest;
import com.example.enums.BookingStatus;
import com.example.model.Booking;
import com.example.repository.BookingRepository;
import com.example.validator.BookingValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingStatusService {

    private final BookingRepository bookingRepository;
    private final BookingValidator bookingValidator;

    @Transactional
    public void updateStatusAuto() {
        LocalDate today = LocalDate.now();

        List<Booking> confirmed = bookingRepository.findByStatus(BookingStatus.CONFIRMED);
        for (Booking booking : confirmed) {
            if (!today.isBefore(booking.getCheckIn()) && !today.isAfter(booking.getCheckOut())) {
                booking.setStatus(BookingStatus.ACTIVE);
            }
        }

        List<Booking> active = bookingRepository.findByStatus(BookingStatus.ACTIVE);
        for (Booking booking : active) {
            if (!booking.getCheckOut().isAfter(today)) {
                booking.setStatus(BookingStatus.COMPLETED);
            }
        }

        bookingRepository.saveAll(confirmed);
        bookingRepository.saveAll(active);
    }

    @Transactional
    public void updateStatusManually(Booking booking, BookingStatus newStatus) {
        BookingStatus current = booking.getStatus();

        switch (current) {
            case PENDING -> {
                switch (newStatus) {
                    case CONFIRMED, REJECTED, CANCELLED -> booking.setStatus(newStatus);
                }
            }
            case CONFIRMED -> {
                switch (newStatus) {
                    case REJECTED, CANCELLED -> booking.setStatus(newStatus);
                }
            }
            case ACTIVE -> {
                switch (newStatus) {
                    case COMPLETED, EXTENSION_REQUESTED -> booking.setStatus(newStatus);
                }
            }
            case EXTENSION_REQUESTED -> {
                switch (newStatus) {
                    case CONFIRMED -> {
                        booking.setStatus(BookingStatus.ACTIVE);
                        booking.setOriginalCheckOut(null);
                    }
                    case REJECTED -> {
                        booking.setCheckOut(booking.getOriginalCheckOut());
                        booking.setOriginalCheckOut(null);
                        booking.setStatus(BookingStatus.ACTIVE);
                    }
                    default -> throw new IllegalArgumentException("Неверный переход статуса из: " + current + " в: " + newStatus);
                }
            }
            case COMPLETED, CANCELLED, REJECTED -> throw new IllegalArgumentException("Ручное обновление из статуса " + current + " не допускается");
            default -> throw new IllegalArgumentException("Неизвестный статус бронирования");
        }

        bookingRepository.save(booking);
    }

    public void handleBookingChangeByStatus(Booking booking, BookingRequest request) {
        BookingStatus status = booking.getStatus();

        switch (status) {
            case ACTIVE -> {
                if (!request.getCheckIn().isEqual(booking.getCheckIn())) {
                    throw new IllegalStateException("Нельзя изменять дату заезда для активного бронирования");
                }
                if (request.getCheckOut().isAfter(booking.getCheckOut())) {
                    booking.setOriginalCheckOut(booking.getCheckOut());
                    booking.setCheckOut(request.getCheckOut());
                    booking.setStatus(BookingStatus.EXTENSION_REQUESTED);
                } else throw new IllegalStateException("Нельзя сократить период активного проживания");

            }
            case CONFIRMED -> {
                bookingValidator.validateDates(request.getCheckIn(), request.getCheckOut());
                booking.setCheckIn(request.getCheckIn());
                booking.setCheckOut(request.getCheckOut());
                booking.setStatus(BookingStatus.EXTENSION_REQUESTED);
            }
            default -> throw new IllegalStateException("Нельзя изменить бронирование со статусом: " + status);
        }
    }
}
