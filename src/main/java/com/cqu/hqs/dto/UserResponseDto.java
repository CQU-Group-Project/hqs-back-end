package com.cqu.hqs.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserResponseDto {
    private Long id;
    private String username;
    private String role;
    private LocalDateTime createdAt;
    private LocalDateTime lastLogin;
}
