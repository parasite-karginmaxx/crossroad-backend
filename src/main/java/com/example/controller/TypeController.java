package com.example.controller;

import com.example.dto.TypeRequest;
import com.example.model.Type;
import com.example.service.TypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/types")
public class TypeController {

    private final TypeService typeService;

    @PostMapping("/add")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Type> createType(@RequestBody TypeRequest request) {
        Type created = typeService.createType(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Type>> getAllTypes() {
        return ResponseEntity.ok(typeService.getAllTypes());
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Type> updateType(@PathVariable Long id, @RequestBody TypeRequest request) {
        Type updated = typeService.updateType(id, request);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteType(@PathVariable Long id) {
        typeService.deleteType(id);
        return ResponseEntity.noContent().build();
    }
}
