package com.example.repository;

import com.example.model.Addition;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdditionRepository extends JpaRepository<Addition, Long> {
    Optional<Addition> findByName(String name);
}

