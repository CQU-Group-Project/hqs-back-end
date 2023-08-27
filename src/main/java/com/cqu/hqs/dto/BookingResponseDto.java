package com.cqu.hqs.dto;

import com.cqu.hqs.utils.PaymentStatus;
import lombok.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Data
public class BookingResponseDto {
    private Long id;
    private Long bookingNumber;
    private LocalDateTime checkInDate;
    private LocalDateTime checkOutDate;
    private double totalAmount;
    private PaymentStatus isPaid;
    private LocalDateTime paymentDate;
    private double discountPercentage;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
}
