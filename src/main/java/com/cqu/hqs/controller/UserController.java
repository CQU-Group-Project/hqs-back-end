package com.cqu.hqs.controller;

import com.cqu.hqs.dto.LoginDto;
import com.cqu.hqs.dto.UserDto;
import com.cqu.hqs.entity.Employee;
import com.cqu.hqs.entity.Guest;
import com.cqu.hqs.entity.User;
import com.cqu.hqs.service.UserService;
import com.cqu.hqs.utils.RestResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private UserService userService;

    public UserController(UserService userService){
        this.userService=userService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDto loginDto){
        return RestResponseDto.success(userService.loginUser(loginDto));
    }
    public void logout(Long userId){

    }
}
