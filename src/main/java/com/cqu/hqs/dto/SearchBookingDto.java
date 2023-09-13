package com.cqu.hqs.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


@NoArgsConstructor
@AllArgsConstructor
@Data
public class SearchBookingDto {
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private int totalNoOfRooms;
    private int guest;
    private String sortOrder;

}
