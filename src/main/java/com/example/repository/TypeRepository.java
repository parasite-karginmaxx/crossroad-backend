package com.example.repository;

import com.example.model.Type;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TypeRepository extends JpaRepository<Type, Long> {
    boolean existsByName(String name);
}
