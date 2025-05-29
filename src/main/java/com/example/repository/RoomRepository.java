package com.example.repository;

import com.example.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoomRepository extends JpaRepository<Room, Long> {
    boolean existsByNumber(String number);
    Optional<Room> findByNumber(String number);
}
