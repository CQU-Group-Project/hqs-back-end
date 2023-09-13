package com.cqu.hqs.controller;

import com.cqu.hqs.dto.BookingDto;
import com.cqu.hqs.dto.EmployeeDto;
import com.cqu.hqs.dto.SearchBookingDto;
import com.cqu.hqs.entity.Booking;
import com.cqu.hqs.entity.Guest;
import com.cqu.hqs.entity.Room;
import com.cqu.hqs.service.BookingService;
import com.cqu.hqs.service.EmployeeService;
import com.cqu.hqs.utils.RestResponseDto;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/api/booking")
public class BookingController {


    private BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }


    @PostMapping
    public ResponseEntity<?> searchRoom(@RequestBody SearchBookingDto searchFilter) {
        return RestResponseDto.success(bookingService.searchAvailableRoom(searchFilter));
    }

    @PostMapping("/confirm")
    private ResponseEntity<?> createRoomBooking(@RequestBody BookingDto bookingDto) throws Exception{
        return RestResponseDto.success(bookingService.confirmBooking(bookingDto));
    }

    @GetMapping
    public ResponseEntity<?> getAllBookings(){
        return RestResponseDto.success(bookingService.getAllBookings());
    }

}
