package com.cqu.hqs.service;

import com.cqu.hqs.Exception.InternalServerErrorException;
import com.cqu.hqs.Exception.ResourceNotFoundException;
import com.cqu.hqs.Repository.BookingRepository;
import com.cqu.hqs.Repository.GuestRepository;
import com.cqu.hqs.Repository.RoomRepository;
import com.cqu.hqs.dto.*;
import com.cqu.hqs.entity.Booking;
import com.cqu.hqs.entity.Employee;
import com.cqu.hqs.entity.Guest;
import com.cqu.hqs.entity.Room;
import com.cqu.hqs.utils.PaymentStatus;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class BookingService {

    private UserService userService;
    private ModelMapper mapper;
    private BookingRepository bookingRepository;
    private RoomRepository roomRepository;
    private GuestRepository guestRepository;

    public BookingService(GuestRepository guestRepository, BookingRepository bookingRepository, UserService userService, ModelMapper modelMapper, RoomRepository roomRepository) {
        this.bookingRepository = bookingRepository;
        this.userService = userService;
        this.mapper = modelMapper;
        this.roomRepository = roomRepository;
        this.guestRepository = guestRepository;
    }

    public List<RoomCombination> searchAvailableRoom(SearchBookingDto searchFilter) {

        LocalDate searchedCheckInDate = searchFilter.getCheckInDate();
        LocalDate searchedCheckOutDate = searchFilter.getCheckOutDate();

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
                System.out.println("Check In Date: " + bookedCheckInDate);
                System.out.println("Check Out Date: " + bookedCheckOutDate);
                System.out.println("S Check In Date: " + searchedCheckInDate);
                System.out.println("S Check Out Date: " + searchedCheckOutDate);

                if ((searchedCheckInDate.isEqual(bookedCheckOutDate) || searchedCheckInDate.isAfter(bookedCheckOutDate)) || (searchedCheckOutDate.isEqual(bookedCheckInDate) || searchedCheckOutDate.isBefore(bookedCheckInDate))) {
                    availableRooms.add(room);
                }
            }
        }

        // Use a recursive function to generate combinations
        List<RoomCombination> roomCombinations = new ArrayList<>();
//        generateCombinations(availableRooms, searchFilter.getTotalNoOfRooms(), new ArrayList<>(), roomCombinations);
        generateCombinations(availableRooms, searchFilter.getTotalNoOfRooms(), new ArrayList<>(), roomCombinations, 0.0, new HashSet<>());
        // Calculate total price for each combination
        for (RoomCombination combination : roomCombinations) {
            double totalPrice = combination.getRooms().stream()
                    .mapToDouble(roomResponseDto -> roomResponseDto.getPrice()) // Assuming you have a getPrice() method in RoomResponseDto
                    .sum();
            combination.setTotalPrice(totalPrice);
        }

        Comparator<RoomCombination> comparator = Comparator.comparingDouble(RoomCombination::getTotalPrice);

        if (searchFilter.getSortOrder().equals("Relevance")) {
            return roomCombinations;
        } else if (searchFilter.getSortOrder().equals("Desc")) {
            if ("Desc".equalsIgnoreCase(searchFilter.getSortOrder())) {
                comparator = comparator.reversed();
            }
        }

//        Comparator<RoomCombination> comparator = Comparator.comparingDouble(RoomCombination::getTotalPrice);
//        if ("Desc".equalsIgnoreCase(searchFilter.getSortOrder())) {
//            comparator = comparator.reversed();
//        }
        roomCombinations = roomCombinations.stream()
                .sorted(comparator)
                .collect(Collectors.toList());

        return roomCombinations;
    }

    private void generateCombinations(List<Room> availableRooms, int totalNoOfRooms, List<RoomResponseDto> currentCombination, List<RoomCombination> roomCombinations, double currentTotalPrice, Set<Long> usedRoomIds) {
        if (totalNoOfRooms == 0) {
            // Sort the combination to ensure consistent ordering
            List<RoomResponseDto> sortedCombination = new ArrayList<>(currentCombination);
            sortedCombination.sort(Comparator.comparing(RoomResponseDto::getId));

            // Check if this combination has already been added
            boolean isDuplicate = roomCombinations.stream()
                    .anyMatch(combination -> combination.getRooms().stream()
                            .map(RoomResponseDto::getId)
                            .collect(Collectors.toList())
                            .equals(sortedCombination.stream()
                                    .map(RoomResponseDto::getId)
                                    .collect(Collectors.toList())));

            if (!isDuplicate) {
                // Add the current combination to the list of combinations
                RoomCombination roomCombination = new RoomCombination();
                roomCombination.setRooms(sortedCombination);
                roomCombination.setTotalPrice(currentTotalPrice);
                roomCombinations.add(roomCombination);
            }
            return;
        }

        for (int i = 0; i < availableRooms.size(); i++) {
            Room room = availableRooms.get(i);

            if (!usedRoomIds.contains(room.getId())) {
                // Calculate the new total price if we add this room
                double newTotalPrice = currentTotalPrice + room.getPrice(); // Assuming you have a getPrice() method in Room class

                // Avoid using the same room more than once in a combination
                List<RoomResponseDto> newCombination = new ArrayList<>(currentCombination);
                newCombination.add(mapToResponseDto(room)); // Convert Room to RoomResponseDto

                Set<Long> newUsedRoomIds = new HashSet<>(usedRoomIds);
                newUsedRoomIds.add(room.getId());

                generateCombinations(availableRooms, totalNoOfRooms - 1, newCombination, roomCombinations, newTotalPrice, newUsedRoomIds);
            }
        }
    }

    private RoomResponseDto mapToResponseDto(Room room) {
        RoomResponseDto roomResponseDto = mapper.map(room, RoomResponseDto.class);
        return roomResponseDto;
    }

    public BookingResponseDto mapToResponseDto(Booking booking) {
        BookingResponseDto bookingResponseDto = mapper.map(booking, BookingResponseDto.class);
        return bookingResponseDto;
    }

    @Transactional
    public String confirmBooking(BookingDto bookingDto) throws Exception {

        Guest guest = guestRepository.findById(bookingDto.getGuestId()).orElseThrow(() -> new ResourceNotFoundException("Guest with id " + bookingDto.getGuestId() + " not found."));
        Booking booking = new Booking();
        booking.setCheckInDate(bookingDto.getCheckInDate());
        booking.setCheckOutDate(bookingDto.getCheckOutDate());
        booking.setCreatedDate(LocalDateTime.now());
        booking.setDiscountPercentage(0);
        booking.setIsPaid(PaymentStatus.UNPAID);
        booking.setTotalAmount(bookingDto.getTotalAmount());
        booking.setBookingNumber("HQS" + Instant.now().toEpochMilli());


        booking.setGuest(guest);

        booking = bookingRepository.save(booking);

        if (null == booking) {
            throw new InternalServerErrorException("Error while confirming booking.");
        }

        for (Long roomId : bookingDto.getRoomId()) {
            Room r = roomRepository.findById(roomId).orElseThrow(() -> new ResourceNotFoundException("Room with id " + roomId + " not found."));
//            if (!r.isAvailable()) {
//                throw new ResourceNotFoundException("Room number " + r.getRoomNumber() + " already booked for the specified date.");
//            }
            r.setAvailable(false);
            r.setBooking(booking);
            r.setUpdatedDate(LocalDateTime.now());
            roomRepository.save(r);

        }

        return "Booked Successfully !";

    }

    public List<BookingResponseDto> getAllBookings() {
//        List<Booking> bookings = bookingRepository.findAll();
//        return bookings.stream().map(booking -> mapToResponseDto(booking)).collect((Collectors.toList()));
        List<Booking> bookings = bookingRepository.findAll(Sort.by(Sort.Direction.DESC, "createdDate"));
        return bookings.stream().map(booking -> mapToResponseDto(booking)).collect((Collectors.toList()));
    }

    private Booking mapToEntity(BookingDto bookingDto) {
        Booking booking = mapper.map(bookingDto, Booking.class);
        return booking;
    }

    private BookingDto mapToDto(Booking b) {
        BookingDto bookingDto = mapper.map(b, BookingDto.class);
        return bookingDto;
    }

}
