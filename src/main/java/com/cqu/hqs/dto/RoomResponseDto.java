package com.cqu.hqs.dto;

import com.cqu.hqs.entity.Booking;
import com.cqu.hqs.entity.RoomType;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RoomResponseDto {

    private Long id;
    private int roomNumber;
    private boolean isAvailable;
    private boolean isClean;
    private double price;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;

    private RoomTypeResponseDto roomType;
    private BookingResponseDto booking;

}
