package com.cqu.hqs.service;

import com.cqu.hqs.Exception.BadRequestException;
import com.cqu.hqs.Repository.EmployeeRepository;
import com.cqu.hqs.Repository.GuestRepository;
import com.cqu.hqs.Repository.UserRepository;
import com.cqu.hqs.dto.*;
import com.cqu.hqs.entity.Employee;
import com.cqu.hqs.entity.Guest;
import com.cqu.hqs.entity.User;
import com.cqu.hqs.utils.UserRoles;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * @author Deependra Karki
 */
@Service
public class UserService {

    private UserRepository userRepository;
    private GuestRepository guestRepository;
    private EmployeeRepository employeeRepository;
    private ModelMapper mapper;

    UserService(UserRepository userRepository, ModelMapper modelMapper, GuestRepository guestRepository, EmployeeRepository employeeRepository) {
        this.userRepository = userRepository;
        this.mapper = modelMapper;
        this.guestRepository = guestRepository;
        this.employeeRepository = employeeRepository;
//        this.guestService=guestService;
    }

    /**
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

        User user = userRepository.findByUsernameAndPassword(loginDto.getUsername(), loginDto.getPassword());
        if (user == null) {
            throw new BadRequestException("Bad Credentials.");
        } else {
            user.setLastLogin(LocalDateTime.now());
            user = userRepository.save(user);
            UserResponseDto userResponseDto = mapToResponseDto(user);

            if (user.getRole().equals("GUEST")) {
//                System.out.println("================GUEST===============");
                userResponseDto.setGuest(mapToGuestDto(guestRepository.findByUser(user)));
            } 
            if (user.getRole().equals("EMPLOYEE")) {
                System.out.println("===============EMPLOYEE==============");
                userResponseDto.setEmployee(mapToEmployeeDto(employeeRepository.findByUserId(user.getId())));
            }

            return userResponseDto;
        }
    }

    public GuestDto mapToGuestDto(Guest guest) {
        GuestDto guestDto = mapper.map(guest, GuestDto.class);
        return guestDto;
    }

    private EmployeeDto mapToEmployeeDto(Employee employee) {
        EmployeeDto employeeDto = mapper.map(employee, EmployeeDto.class);
        return employeeDto;
    }
}
