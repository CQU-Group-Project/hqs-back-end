package com.cqu.hqs.entity;

import com.cqu.hqs.utils.PaymentStatus;
import jakarta.persistence.*;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Component
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String bookingNumber;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private double totalAmount;

    @Enumerated(EnumType.STRING)
    private PaymentStatus isPaid;
    private LocalDateTime paymentDate;
    private double discountPercentage;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;

    @ManyToOne
    @JoinColumn(name = "guest_id")
    private Guest guest;

    @OneToOne(mappedBy = "booking", cascade = CascadeType.ALL)
    private BookingEmailLog bookingEmailLog;

    @OneToMany
    @JoinColumn(name = "booking_id")
    private List<Room> rooms;


    @Override
    public String toString() {
        return "Booking{" +
                "id=" + id +
                ", bookingNumber=" + bookingNumber +
                ", checkInDate=" + checkInDate +
                ", checkOutDate=" + checkOutDate +
                ", totalAmount=" + totalAmount +
                ", isPaid=" + isPaid +
                ", paymentDate=" + paymentDate +
                ", discountPercentage=" + discountPercentage +
                ", createdDate=" + createdDate +
                ", updatedDate=" + updatedDate +
                ", guest=" + guest +
                ", bookingEmailLog=" + bookingEmailLog +
                ", rooms=" + rooms +
                '}';
    }
}
