package com.cqu.hqs.dto;

import com.cqu.hqs.utils.UserRoles;
import lombok.Data;

@Data
public class UserDto {
    private String email;
    private String password;
    private UserRoles role;
}
