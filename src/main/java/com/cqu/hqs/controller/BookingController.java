package com.cqu.hqs.controller;

import com.cqu.hqs.dto.SearchBookingDto;
import com.cqu.hqs.entity.Booking;
import com.cqu.hqs.entity.Guest;
import com.cqu.hqs.entity.Room;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/booking")
public class BookingController {

    private Booking booking;

    private SearchBookingDto searchFilter;

    private  BookingController(){
        this.booking=booking;
    }

    private List<Room> searchRoom(SearchBookingDto searchFilter){
        return null;
    }

    private void createRoomBooking(Guest guest, List<Room> rooms){

    }

    private void createEmailLog(Booking booking){

    }

    private void confirmBooking(int bookingId){

    }

    private Booking viewBookingReport(int bookingId){
        return null;
    }
}
