package com.cqu.hqs.service;

import com.cqu.hqs.Exception.BadRequestException;
import com.cqu.hqs.Exception.InternalServerErrorException;
import com.cqu.hqs.Exception.ResourceNotFoundException;
import com.cqu.hqs.Repository.GuestRepository;
import com.cqu.hqs.dto.GuestDto;
import com.cqu.hqs.dto.GuestEditDto;
import com.cqu.hqs.dto.GuestResponseDto;
import com.cqu.hqs.entity.Guest;
import com.cqu.hqs.entity.User;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Deependra Karki
 */
@Service
public class GuestService {

    private UserService userService;
    private GuestRepository guestRepository;
    private BookingService bookingService;
    private ModelMapper mapper;

    public GuestService(GuestRepository guestRepository, UserService userService, BookingService bookingService, ModelMapper mapper) {
        this.guestRepository = guestRepository;
        this.userService = userService;
        this.mapper = mapper;
        this.bookingService = bookingService;
    }

    @Transactional
    public GuestDto save(GuestDto guestDto) {

        if (guestRepository.existsByEmail(guestDto.getEmail())) {
            throw new BadRequestException("Email Already Exists.");
        }
        User user = userService.createUser(guestDto);
        Guest guest = mapToEntity(guestDto);
        guest.setCreatedDate(LocalDateTime.now());
        guest.setUser(user);
        guest = guestRepository.save(guest);
        if (null == guest) {
            throw new InternalServerErrorException("Error while saving guest");
        }
        return mapToDto(guest);

    }

    public List<GuestResponseDto> getAllGuests() {
        List<Guest> guests = guestRepository.findAll();
        return guests.stream().map(guest -> mapToResponseDto(guest)).collect((Collectors.toList()));
    }




    @Transactional
    public GuestEditDto editGuest(GuestEditDto guestDto) {
        Guest guest = guestRepository.findById(guestDto.getId()).orElseThrow(() -> new ResourceNotFoundException("Guest with id " + guestDto.getId() + " not found."));
        if (guestDto.getFirstName() != null)
            guest.setFirstName(guestDto.getFirstName());
        if (guestDto.getLastName() != null)
            guest.setLastName(guestDto.getLastName());
        if (guestDto.getAddress() != null)
            guest.setAddress(guestDto.getAddress());
        if (guestDto.getCity() != null)
            guest.setCity(guestDto.getCity());
        if (guestDto.getState() != null)
            guest.setState(guestDto.getState());
        if (guestDto.getCountry() != null)
            guest.setCountry(guestDto.getCountry());
        if (guestDto.getPostalCode() != null)
            guest.setPostalCode(guestDto.getPostalCode());
        if (guestDto.getPhone() != null)
            guest.setPhone(guestDto.getPhone());
        if (guestDto.getEmail() != null) {
            if (guestRepository.existsByEmail(guestDto.getEmail())) {
                throw new BadRequestException("Email Already Exists.");
            } else {
                guest.setEmail(guestDto.getEmail());
            }
        }

        guest.setUpdatedDate(LocalDateTime.now());
        guest=guestRepository.save(guest);
        return mapToEditDto(guest);
    }

    public GuestDto mapToDto(Guest guest) {
        GuestDto guestDto = mapper.map(guest, GuestDto.class);
        return guestDto;
    }

    public GuestEditDto mapToEditDto(Guest guest) {
        GuestEditDto guestDto = mapper.map(guest, GuestEditDto.class);
        return guestDto;
    }

    public Guest mapToEntity(GuestDto guestDto) {
        Guest guest = mapper.map(guestDto, Guest.class);
        return guest;
    }

    public GuestResponseDto mapToResponseDto(Guest guest) {
        GuestResponseDto guestResponseDto = mapper.map(guest, GuestResponseDto.class);
//        guestResponseDto.setUser(userService.mapToResponseDto(guest.getUser()));
        guestResponseDto.setBookings(guest.getBookings().stream().map(booking -> bookingService.mapToResponseDto(booking)).collect(Collectors.toList()));
        return guestResponseDto;
    }
}
