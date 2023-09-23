package com.cqu.hqs.dto;

import com.cqu.hqs.entity.Employee;
import com.cqu.hqs.entity.Guest;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserResponseDto {
    private Long id;
    private String username;
    private String role;
    private LocalDateTime createdAt;
    private LocalDateTime lastLogin;
    private GuestDto guest;
    private EmployeeDto employee;

}
