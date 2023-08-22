package com.cqu.hqs.controller;

import com.cqu.hqs.entity.Employee;
import com.cqu.hqs.entity.Guest;
import com.cqu.hqs.entity.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private User user;

    public List<User> listAllUser(){
        return null;
    }
    public void login(String email, String password){

    }
    public void logout(Long userId){

    }
}
