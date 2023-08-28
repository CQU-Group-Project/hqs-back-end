package com.cqu.hqs.service;

import com.cqu.hqs.Exception.BadRequestException;
import com.cqu.hqs.Repository.UserRepository;
import com.cqu.hqs.dto.*;
import com.cqu.hqs.entity.Guest;
import com.cqu.hqs.entity.User;
import jakarta.transaction.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

/**
 *
 * @author Deependra Karki
 */
@Service
public class UserService {

    private UserRepository userRepository;
    private ModelMapper mapper;

    UserService(UserRepository userRepository, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.mapper=modelMapper;
    }

//    @Transactional
//    User createUser(GuestDto guestDto) {
//        User user = new User();
//        user.setUsername(guestDto.getEmail());
//        user.setPassword(guestDto.getPassword());
//        user.setRole(String.valueOf(guestDto.getRole()));
//        user.setCreatedAt(LocalDateTime.now());
//        return userRepository.save(user);
//    }

//    @Transactional
//    public <T extends UserDto> User createUser(T dto) {
//        User user = new User();
//        user.setUsername(dto.getEmail());
//        user.setPassword(dto.getPassword());
//        user.setRole(String.valueOf(dto.getRole()));
//        user.setCreatedAt(LocalDateTime.now());
//        return userRepository.save(user);
//    }

    /**
     *
     * @param dto
     * @param <T>
     * @return
     */
    @Transactional
    public <T> User createUser(T dto) {
        User user = new User();

        if (dto instanceof GuestDto) {
            GuestDto guestDto = (GuestDto) dto;
            user.setUsername(guestDto.getEmail());
            user.setPassword(guestDto.getPassword());
            user.setRole(String.valueOf(guestDto.getRole()));
        } else if (dto instanceof EmployeeDto) {
            EmployeeDto employeeDto = (EmployeeDto) dto;
            user.setUsername(employeeDto.getEmail());
            user.setRole(String.valueOf(employeeDto.getRole()));
        }

        user.setCreatedAt(LocalDateTime.now());
        return userRepository.save(user);
    }


    public UserResponseDto mapToResponseDto(User user) {
        UserResponseDto userResponseDto = mapper.map(user, UserResponseDto.class);
        return userResponseDto;
    }

    public UserResponseDto loginUser(LoginDto loginDto) {

        User user=userRepository.findByUsernameAndPassword(loginDto.getUsername(),loginDto.getPassword());
        if(user==null){
            throw new BadRequestException("Bad Credentials.");
        }
        else{
            user.setLastLogin(LocalDateTime.now());
            user=userRepository.save(user);
            return mapToResponseDto(user);
        }
    }
}
