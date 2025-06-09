package com.example.controller.user;

import com.example.dto.request.BookingRequest;
import com.example.model.User;
import com.example.service.BookingService;
import com.example.service.UserService;
import com.example.util.ControllerUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Бронирования (Пользователь)", description = "Эндпоинты для клиентов")
@RestController
@RequestMapping("/api/user/bookings")
@RequiredArgsConstructor
@PreAuthorize("hasRole('USER')")
public class UserBookingController {

    private final BookingService bookingService;
    private final UserService userService;

    @Operation(summary = "Добавление бронирования")
    @PostMapping("/add")
    public ResponseEntity<?> createBooking(
            @RequestBody BookingRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        User user = ControllerUtils.resolveUser(userDetails, userService);
        return ResponseEntity.ok(bookingService.createBooking(request, user));
    }

    @Operation(summary = "Получение бронирований текущего пользователя")
    @GetMapping("/my")
    public ResponseEntity<?> getUserBookings(@AuthenticationPrincipal UserDetails userDetails) {
        User user = ControllerUtils.resolveUser(userDetails, userService);
        return ResponseEntity.ok(bookingService.getUserBookings(user));
    }

    @Operation(summary = "Изменение текущего бронирования")
    @PutMapping("/{id}/edit")
    public ResponseEntity<?> updateBooking(
            @PathVariable Long id,
            @RequestBody BookingRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        User user = ControllerUtils.resolveUser(userDetails, userService);
        return ResponseEntity.ok(bookingService.updateBooking(id, request, user));
    }

    @Operation(summary = "Отмена текущего бронирования")
    @PutMapping("/{id}/cancel")
    public ResponseEntity<String> cancelBooking(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        User user = ControllerUtils.resolveUser(userDetails, userService);
        bookingService.cancelBookingByUserOrThrow(id, user);
        return ResponseEntity.ok("Бронирование отменено");
    }
}
