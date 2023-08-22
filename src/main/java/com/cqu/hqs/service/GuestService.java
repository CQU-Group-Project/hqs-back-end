package com.cqu.hqs.service;

import com.cqu.hqs.Exception.BadRequestException;
import com.cqu.hqs.Exception.InternalServerErrorException;
import com.cqu.hqs.Repository.GuestRepository;
import com.cqu.hqs.dto.GuestDto;
import com.cqu.hqs.entity.Guest;
import com.cqu.hqs.entity.User;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

/**
 *
 * @author Deependra Karki
 */
@Service
public class GuestService {

    private UserService userService;
    private GuestRepository guestRepository;
    private ModelMapper mapper;

    public GuestService(GuestRepository guestRepository, UserService userService, ModelMapper mapper) {
        this.guestRepository = guestRepository;
        this.userService = userService;
        this.mapper = mapper;
    }

    @Transactional
    public GuestDto save(GuestDto guestDto) {

        User user = userService.createUser(guestDto);
        if (guestRepository.existsByEmail(guestDto.getEmail())) {
            throw new BadRequestException("Email Already Exists.");
        }
        Guest guest = mapToEntity(guestDto);
        guest.setCreatedDate(LocalDateTime.now());
        guest.setUser(user);
        guest = guestRepository.save(guest);
        if (null == guest) {
            throw new InternalServerErrorException("Error while saving user");
        }
        return mapToDto(guest);

    }

    private GuestDto mapToDto(Guest guest) {
        GuestDto guestDto = mapper.map(guest, GuestDto.class);
        return guestDto;
    }

    private Guest mapToEntity(GuestDto guestDto) {
        Guest guest = mapper.map(guestDto, Guest.class);
        return guest;
    }

}
