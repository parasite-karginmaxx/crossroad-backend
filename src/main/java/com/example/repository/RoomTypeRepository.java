package com.example.repository;

import com.example.model.RoomType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomTypeRepository extends JpaRepository<RoomType, Long> {
    boolean existsByName(String name);
}
