package com.example.validator;

import com.example.enums.BookingStatus;
import com.example.exception.IncompleteProfileException;
import com.example.model.Booking;
import com.example.model.Room;
import com.example.model.User;
import com.example.repository.BookingRepository;
import com.example.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class BookingValidator {
    private final BookingRepository bookingRepository;
    private final RoomRepository roomRepository;

    public void validateUserProfile(User user) {
        var profile = user.getProfile();
        if (profile == null) {
            throw new IncompleteProfileException("Профиль отсутствует");
        }

        if (isBlank(profile.getFirstName())) {
            throw new IncompleteProfileException("Имя обязательно");
        } else if (isBlank(profile.getLastName())) {
            throw new IncompleteProfileException("Фамилия обязательна");
        } else if (isBlank(profile.getPassportNumber())) {
            throw new IncompleteProfileException("Паспортные данные обязательны");
        } else if (isBlank(profile.getPhone())) {
            throw new IncompleteProfileException("Номер телефона обязателен");
        }
    }

    public void validateDates(LocalDate checkIn, LocalDate checkOut) {
        if (checkIn.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Дата заезда не может быть в прошлом");
        }
        if (checkOut.isBefore(checkIn)) {
            throw new IllegalArgumentException("Дата выезда должна быть позже даты заезда");
        }
    }

    public boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }

    public void checkOwner(Booking booking, User user) {
        if (!booking.getUser().getId().equals(user.getId())) {
            throw new AccessDeniedException("Вы не можете редактировать чужое бронирование");
        }
    }

    public boolean isFinalStatus(BookingStatus status) {
        return List.of(BookingStatus.CANCELLED, BookingStatus.REJECTED, BookingStatus.COMPLETED).contains(status);
    }

    public Room getRoomOrThrow(Long roomId) {
        return roomRepository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("Комната не найдена"));
    }

    public void checkUserAlreadyBookedThisRoom(User user, Room room, LocalDate checkIn, LocalDate checkOut) {
        List<Booking> userConflicts = bookingRepository
                .findByUserAndRoomAndStatusInAndDatesOverlap(
                        user,
                        room,
                        List.of(
                                BookingStatus.PENDING,
                                BookingStatus.CONFIRMED,
                                BookingStatus.ACTIVE,
                                BookingStatus.EXTENSION_REQUESTED
                        ),
                        checkIn,
                        checkOut
                );

        if (!userConflicts.isEmpty()) {
            throw new IllegalStateException("У вас уже есть бронирование этой комнаты на выбранные даты");
        }
    }

    public void checkRoomIsAvailable(Long roomId, LocalDate checkIn, LocalDate checkOut) {
        List<Booking> conflicts = bookingRepository.findConflictingBookings(roomId, checkIn, checkOut);
        if (!conflicts.isEmpty()) {
            throw new IllegalStateException("Комната уже забронирована на выбранные даты");
        }
    }
}
