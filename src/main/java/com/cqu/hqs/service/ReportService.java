package com.cqu.hqs.service;

import com.cqu.hqs.Repository.BookingRepository;
import com.cqu.hqs.Repository.EmployeeRepository;
import com.cqu.hqs.Repository.GuestRepository;
import com.cqu.hqs.Repository.RoomRepository;
import com.cqu.hqs.dto.ReportResponseDto;
import com.cqu.hqs.dto.RevenueResponse;
import com.cqu.hqs.entity.Booking;
import com.cqu.hqs.entity.Employee;
import com.cqu.hqs.entity.Guest;
import com.cqu.hqs.entity.Room;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ReportService {

    private ModelMapper mapper;
    private EmployeeRepository employeeRepository;
    private RoomRepository roomRepository;
    private BookingRepository bookingRepository;
    private GuestRepository guestrepositor;

    public ReportService(ModelMapper mapper, EmployeeRepository employeeRepository, RoomRepository roomRepository, BookingRepository bookingRepository, GuestRepository guestRepository) {
        this.mapper = mapper;
        this.employeeRepository = employeeRepository;
        this.roomRepository = roomRepository;
        this.bookingRepository = bookingRepository;
        this.guestrepositor = guestRepository;
    }

    public ReportResponseDto getReport() {
        ReportResponseDto rs = new ReportResponseDto();
        // Employee Count
        List<Employee> employeeList = employeeRepository.findAll();
        rs.setTotalEmployee(employeeList.size());

        // Bookings Count
        List<Booking> bookingList = bookingRepository.findAll();
        rs.setTotalBookings(bookingList.size());

        //Room occupancy
        List<Room> roomList = roomRepository.findAll();
        List<Room> availableRooms = findAvailableRooms();
        int totalRooms = roomList.size();
        int aRoom = availableRooms.size();
        System.out.println("total rooms:" + totalRooms + "  Available Rooms: " + aRoom + "Percentage :" + (aRoom / (double) totalRooms));
        rs.setRoomOccupancy((aRoom / (double) totalRooms) * 100);

        //Total Guests
        List<Guest> guestList = guestrepositor.findAll();
        rs.setTotalGuests(guestList.size());
        rs.setRevenueResponse(getRevenue());
        return rs;
    }

    public List<Room> findAvailableRooms() {
        LocalDate searchedCheckInDate = LocalDate.now();

        //Fetch All rooms
        List<Room> rooms = roomRepository.findAll(Sort.by(Sort.Direction.DESC, "createdDate"));

        List<Room> availableRooms = new ArrayList<>();

        for (Room room : rooms) {
            //Add rooms to available Rooms List if the available flag is true
            System.out.println("Room Availability:" + room.isAvailable());
            if (room.isAvailable() && room.getBooking() == null) {
                availableRooms.add(room);
            } else {
                LocalDate bookedCheckInDate = room.getBooking().getCheckInDate();
                LocalDate bookedCheckOutDate = room.getBooking().getCheckOutDate();

                if ((searchedCheckInDate.isEqual(bookedCheckOutDate) || searchedCheckInDate.isAfter(bookedCheckOutDate)) || (searchedCheckInDate.isBefore(bookedCheckInDate))) {
                    availableRooms.add(room);
                }
            }
        }

        return availableRooms;
    }

    public Map<LocalDate, Double> getRevenue() {
        List<Booking> bookings = bookingRepository.findAll(Sort.by(Sort.Direction.DESC, "createdDate"));

        // Group the bookings by createdDate and calculate the sum of totalAmount for each group
//        Map<LocalDateTime, Double> totalAmountByDate = bookings.stream()
//                .collect(Collectors.groupingBy(
//                        Booking::getCreatedDate,
//                        Collectors.summingDouble(Booking::getTotalAmount)
//                ));
        Map<LocalDate, Double> totalAmountByDate = bookings.stream()
                .collect(Collectors.groupingBy(
                        booking -> booking.getCreatedDate().toLocalDate(),
                        Collectors.summingDouble(Booking::getTotalAmount)
                ));

        TreeMap<LocalDate, Double> sortedTotalAmountByDate = new TreeMap<>(totalAmountByDate);

        return sortedTotalAmountByDate;
    }
}
