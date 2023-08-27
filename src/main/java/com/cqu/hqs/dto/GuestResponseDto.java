package com.cqu.hqs.dto;

import com.cqu.hqs.entity.Booking;
import com.cqu.hqs.entity.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class GuestResponseDto {
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
    private UserResponseDto user;
    private List<BookingResponseDto> bookings;
}
