package com.cqu.hqs.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Guest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstName;
    private String lastName;
    private String address;
    private String city;
    private String state;
    private String country;
    private String postalCode;
    private String phone;
    private String email;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;

    @OneToOne
    @JoinColumn(name = "user_id", unique = true)
    @JsonIgnore
    private User user;

    @OneToMany(mappedBy = "guest", cascade = CascadeType.ALL)
    private List<Booking> bookings;
}
