package com.example.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String number;
    private int pricePerNight;
    private String description;

    @ManyToOne
    @JoinColumn(name = "type_id")
    @JsonBackReference
    private Type type;

    @OneToMany(mappedBy = "room")
    @JsonManagedReference
    private List<Booking> bookings = new ArrayList<>();
}

