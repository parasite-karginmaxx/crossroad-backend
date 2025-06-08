package com.example.model;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.*;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;
    private String email;

    @Column(name = "password_hash", nullable = false, length = 100)
    private String password;

    @Column(nullable = false, updatable = false)
    private LocalDate registrationDate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id")
    private Role role;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    @ToString.Exclude
    @JsonManagedReference
    private UserProfile profile;

    @OneToMany(mappedBy = "user")
    @ToString.Exclude
    @JsonManagedReference
    private List<Booking> bookings = new ArrayList<>();
}
