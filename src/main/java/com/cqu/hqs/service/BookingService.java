package com.cqu.hqs.service;

import com.cqu.hqs.Repository.BookingRepository;
import com.cqu.hqs.dto.BookingResponseDto;
import com.cqu.hqs.entity.Booking;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

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
}
