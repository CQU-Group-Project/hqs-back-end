package com.cqu.hqs.dto;

import com.cqu.hqs.utils.UserRoles;
import java.time.LocalDateTime;
import lombok.Data;

/**
 *
 * @author Deependra Karki
 */

@Data
public class GuestDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String address;
    private String city;
    private String state;
    private String country;
    private String postalCode;
    private String phone;
    private String email;
    private String password;
    private String confirmPassword;
    private UserRoles role;
}
