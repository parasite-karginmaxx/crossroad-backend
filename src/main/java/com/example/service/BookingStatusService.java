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

        if (current == BookingStatus.EXTENSION_REQUESTED || current == BookingStatus.PENDING) {
            switch (newStatus) {
                case CONFIRMED -> {
                    booking.setStatus(BookingStatus.CONFIRMED);
                    booking.setOriginalCheckOut(null);
                }
                case REJECTED -> {
                    if (booking.getOriginalCheckOut() != null) {
                        booking.setCheckOut(booking.getOriginalCheckOut());
                    }
                    booking.setOriginalCheckOut(null);
                    booking.setStatus(BookingStatus.ACTIVE);
                }
                case CANCELLED, COMPLETED -> booking.setStatus(newStatus);
                default -> throw new IllegalArgumentException("Неверный переход статуса");
            }
        } else {
            booking.setStatus(newStatus); // Прямой переход
        }

        bookingRepository.save(booking);
    }

    public void handleActiveBookingExtension(Booking booking, BookingRequest request) {
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

    public void handleConfirmedBookingModification(Booking booking, BookingRequest request) {
        bookingValidator.validateDates(request.getCheckIn(), request.getCheckOut());

        booking.setCheckIn(request.getCheckIn());
        booking.setCheckOut(request.getCheckOut());
        booking.setStatus(BookingStatus.PENDING);
    }
}
