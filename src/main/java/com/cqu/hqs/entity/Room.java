package com.cqu.hqs.entity;

import com.cqu.hqs.utils.RoomCleanliness;
import com.cqu.hqs.utils.RoomStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
//@Data
@NoArgsConstructor
@AllArgsConstructor
@Component
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int roomNumber;
    @Enumerated(EnumType.STRING)
    private RoomStatus isAvailable;

    @Enumerated(EnumType.STRING)
    private RoomCleanliness isClean;

    private LocalDateTime lastCleanedDate;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;

    @OneToOne
    @JoinColumn(name = "room_type_id", unique = true)
    private RoomType roomType;

    @ManyToOne
    @JoinColumn(name = "booking_id")
    private Booking booking;


}
