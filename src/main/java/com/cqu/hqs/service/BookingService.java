package com.cqu.hqs.service;

import com.cqu.hqs.Repository.BookingRepository;
import com.cqu.hqs.dto.BookingResponseDto;
import com.cqu.hqs.entity.Booking;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookingService {

    private UserService userService;
    private ModelMapper mapper;
    private BookingRepository bookingRepository;

    public BookingService(BookingRepository bookingRepository,UserService userService, ModelMapper modelMapper) {
        this.bookingRepository = bookingRepository;
        this.userService = userService;
        this.mapper = modelMapper;
    }

    public BookingResponseDto mapToResponseDto(Booking booking) {
        BookingResponseDto bookingResponseDto = mapper.map(booking, BookingResponseDto.class);
        return bookingResponseDto;
    }

    public List<BookingResponseDto> getAllBookings() {
//        List<Booking> bookings = bookingRepository.findAll();
//        return bookings.stream().map(booking -> mapToResponseDto(booking)).collect((Collectors.toList()));
        List<Booking> bookings = bookingRepository.findAll(Sort.by(Sort.Direction.DESC, "createdDate"));
        return bookings.stream().map(booking -> mapToResponseDto(booking)).collect((Collectors.toList()));
    }
}
