package com.cqu.hqs.dto;

import com.cqu.hqs.entity.Guest;
import com.cqu.hqs.entity.Room;
import com.cqu.hqs.utils.PaymentStatus;
import lombok.*;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class BookingResponseDto {
    private Long id;
    private String bookingNumber;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private double totalAmount;
    private PaymentStatus isPaid;
    private LocalDateTime paymentDate;
    private double discountPercentage;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
    private List<RoomResponseDto> roomList;
//    private GuestResponseDto guest;
}
