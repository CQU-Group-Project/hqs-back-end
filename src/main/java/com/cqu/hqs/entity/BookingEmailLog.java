package com.cqu.hqs.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
//@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingEmailLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String emailTo;
    private String subject;
    private String content;
    private LocalDateTime sentAt;

    @OneToOne
    @JoinColumn(name = "booking_id", unique = true)
    private Booking booking;
}
