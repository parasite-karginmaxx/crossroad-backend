package com.example.repository;

import com.example.enums.BookingStatus;
import com.example.model.Booking;
import com.example.model.Room;
import com.example.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByUser(User user);
    List<Booking> findByRoomId(Long roomId);
    List<Booking> findByStatus(BookingStatus status);

    @Query("""
    SELECT b FROM Booking b
    WHERE b.room.id = :roomId
      AND b.status NOT IN ('PENDING', 'CANCELLED', 'REJECTED', 'COMPLETED')
      AND (
        (:checkIn BETWEEN b.checkIn AND b.checkOut)
        OR (:checkOut BETWEEN b.checkIn AND b.checkOut)
        OR (b.checkIn BETWEEN :checkIn AND :checkOut)
      )
    """)
    List<Booking> findConflictingBookings(Long roomId, LocalDate checkIn, LocalDate checkOut);

    @Query("""
    SELECT b FROM Booking b
    WHERE b.user = :user
      AND b.room = :room
      AND b.status IN :statuses
      AND (b.checkIn < :checkOut AND b.checkOut > :checkIn)
""")
    List<Booking> findByUserAndRoomAndStatusInAndDatesOverlap(
            @Param("user") User user,
            @Param("room") Room room,
            @Param("statuses") List<BookingStatus> statuses,
            @Param("checkIn") LocalDate checkIn,
            @Param("checkOut") LocalDate checkOut
    );
}
