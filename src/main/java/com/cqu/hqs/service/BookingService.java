package com.cqu.hqs.service;

import com.cqu.hqs.Exception.InternalServerErrorException;
import com.cqu.hqs.Exception.ResourceNotFoundException;
import com.cqu.hqs.Repository.BookingRepository;
import com.cqu.hqs.Repository.GuestRepository;
import com.cqu.hqs.Repository.RoomRepository;
import com.cqu.hqs.config.EmailNotification;
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
        List<Room> rooms = roomRepository.findAll(Sort.by(Sort.Direction.DESC, "createdDate")).stream().filter(room-> room.getStatus().equals("ACTIVE")).collect((Collectors.toList()));

        List<Room> availableRooms = new ArrayList<>();

        for (Room room : rooms) {
            //Add rooms to available Rooms List if the available flag is true
            System.out.println("Room Availability:" + room.isAvailable());
            if (room.isAvailable() && room.getBooking() == null ) {
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
        bookingResponseDto.setRoomList(booking.getRooms().stream().map(room -> mapToRoomResponseDto(room)).collect(Collectors.toList()));
        return bookingResponseDto;
    }

    public RoomResponseDto mapToRoomResponseDto(Room room) {
        RoomResponseDto roomResponseDto = mapper.map(room, RoomResponseDto.class);
        return roomResponseDto;
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

        sendBookingEmail(guest.getEmail(), guest.getFirstName());

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

    public void sendBookingEmail(String email, String name) {
        EmailNotification e = new EmailNotification();
        e.setFrom("noreply@hotelqueensland.com.au");
        String[] toList = {email};
        e.setTo(toList);
        e.setSubject("Booking Confirmation");
        e.setBody("<!DOCTYPE html><html><head> <meta charset='UTF-8'> <meta name='viewport' content='width=device-width' /> <meta name='description'> <meta http-equiv='Content-Type' content='text/html; charset=UTF-8'> <title>Hotel Queensland System</title> <style type='text/css'> @media only screen and (max-width:580px) { .span-2, .span-3 { max-width: none !important; width: 100% !important; } .span-2>table, .span-3>table { max-width: 100% !important; width: 100% !important; } } @media all { a { text-decoration: none !important; } .btn-primary table td:hover { background-color: #3e8ddd !important; } .btn-primary a:hover { background-color: #ffffff !important; border-color: #3e8ddd !important; color: #3e8ddd !important; } } @media only screen and (max-width:580px) { h1 { font-size: 28px !important; margin-top: 20px !important; margin-bottom: 10px !important; } h2 { font-size: 22px !important; margin-bottom: 10px !important; } h3 { font-size: 16px !important; margin-bottom: 10px !important; } .main p, .main ul, .main ol, .main td, .main span { font-size: 16px !important; } .wrapper { padding: 0 10px 20px !important; } .article { padding-left: 0 !important; padding-right: 0 !important; } .content { padding: 0 !important; } .container { padding: 0 !important; width: 100% !important; } .header { margin-bottom: 0px !important; } .main { border-left-width: 0 !important; border-radius: 0 !important; border-right-width: 0 !important; } .btn table { max-width: 100% !important; width: 100% !important; } .btn a { max-width: 100% !important; padding: 12px 5px !important; width: 100% !important; } .img-responsive { height: auto !important; max-width: 100% !important; width: auto !important; } .alert td { border-radius: 0 !important; padding: 10px !important; } .receipt { width: 100% !important; } hr { margin-bottom: 10px !important; margin-top: 10px !important; } .hr tr:first-of-type td, .hr tr:last-of-type td { height: 10px !important; line-height: 10px !important; } } </style></head><body style='font-family:Lato, Helvetica, sans-serif; -webkit-font-smoothing:antialiased; font-size:14px; line-height:1.4; -ms-text-size-adjust:100%; -webkit-text-size-adjust:100%; background-color:#f6f6f6; margin:0; padding:0;'> <table border='0' cellpadding='0' cellspacing='0' class='body' style='border-collapse:separate; mso-table-lspace:0pt; mso-table-rspace:0pt; width:100%; background-color:#F2F2F2;' width='100%' bgcolor='#F2F2F2'> <tr> <td style='vertical-align:top;' valign='top'>&nbsp;</td> <td class='container' style='font-family:Lato, Helvetica, sans-serif; font-size:14px; vertical-align:top; margin:0 auto !important; max-width:580px; padding:0 10px 10px; width:580px;' width='580' valign='top'> <div class='content' style='box-sizing:border-box; display:block; margin:0 auto; max-width:580px; padding:0 0 10px;'> <table border='0' cellpadding='0' cellspacing='0' class='main' style='border-collapse:separate; mso-table-lspace:0pt; mso-table-rspace:0pt; width:100%; background:#fff; border-radius:.2em;' width='100%'> <tr> <td style='font-family:Lato, Helvetica, sans-serif; font-size:14px; vertical-align:top;' valign='top'> <img src='https://images.unsplash.com/photo-1615460549969-36fa19521a4f?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=3774&q=80' alt='Learning & Educating Through Uncertainty' width='562' height='265' class='img-responsive' style='border:none; -ms-interpolation-mode:bicubic; max-width:100%; min-width:100%; object-fit: cover;'> </td> </tr> <!-- START MAIN CONTENT AREA --> <tr> <td class='wrapper' style='font-family:Lato, Helvetica, sans-serif; font-size:14px; vertical-align:top; box-sizing:border-box; padding:20px;' valign='top'> <table border='0' cellpadding='0' cellspacing='0' style='border-collapse:separate; mso-table-lspace:0pt; mso-table-rspace:0pt; width:100%;' width='100%'> <tr> <td style='font-family:Lato, Helvetica, sans-serif; font-size:14px; vertical-align:top;' valign='top'> <h6 style='color:#222222; font-family:Lato, Helvetica, sans-serif; font-weight:300; line-height:1.1; margin:0; margin-bottom:30px; font-size:25px; text-align:left;'> Dear " + name + " ,</h6> <p style='font-size: 16px; text-align: justify;'> We are excited to confirm your reservation at Hotel Queensland. We look forward to providing you with a memorable and comfortable stay. </p> <p style='font-size: 16px; text-align: justify;'>If you have any questions, require further assistance, or need to make any changes to your reservation, please do not hesitate to contact us at <b>045 185 691</b> or email at <b>booking@hotelqueensland.com.au</b> </p> <p style='font-size: 16px; text-align: justify;'>Thank you for choosing Hotel Queensland. We cannot wait to welcome you!</p> </td> </tr> </table> </td> </tr> <tr> <td class='wrapper section-callout' style='font-family:Lato, Helvetica, sans-serif; font-size:14px; vertical-align:top; box-sizing:border-box; padding:24px 20px 30px; background-color:#c1db8a; color:#373737;' valign='top' bgcolor='#D13811'> <table border='0' cellpadding='0' cellspacing='0' style='border-collapse:separate; mso-table-lspace:0pt; mso-table-rspace:0pt; width:100%;' width='100%'> <tr> <td style='font-family:Lato, Helvetica, sans-serif; vertical-align:top; font-size:12px; color:#999999; text-align:center;' valign='top' align='center'> <a href=' style=' text-decoration:underline; color:#999999; font-size:12px; text-align:center;'> <img width='48' height='48' src='https://img.icons8.com/fluency/96/facebook-new.png' alt='facebook-new'/> </a>&nbsp; <a href=' style=' text-decoration:underline; color:#999999; font-size:12px; text-align:center;'> <img width='48' height='48' src='https://img.icons8.com/color/48/twitter-circled--v1.png' alt='twitter-circled--v1'/> </a>&nbsp; <a href=' style=' text-decoration:underline; color:#999999; font-size:12px; text-align:center;'> <img width='48' height='48' src='https://img.icons8.com/fluency/48/instagram-new.png' alt='instagram-new'/> </a>&nbsp; </td> </tr> </table> </td> </tr> <!-- END CALL OUT --> </table> <!-- START FOOTER --> </div> </td> <td style='vertical-align:top;' valign='top'>&nbsp;</td> </tr> </table></body></html>");
        // Check if all required properties are initialized before sending the email
        if (e.getFrom() != null && e.getTo() != null && e.getSubject() != null && e.getBody() != null) {
            System.out.println("2");
            e.sendEmail();
        } else {
            System.err.println("Email properties are not properly initialized.");
        }
    }
}
