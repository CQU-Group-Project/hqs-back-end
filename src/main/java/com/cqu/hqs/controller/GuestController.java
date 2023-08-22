package com.cqu.hqs.controller;

import com.cqu.hqs.dto.GuestDto;
import com.cqu.hqs.entity.Employee;
import com.cqu.hqs.entity.Guest;
import com.cqu.hqs.entity.User;
import com.cqu.hqs.service.GuestService;
import com.cqu.hqs.service.UserService;
import com.cqu.hqs.utils.RestResponseDto;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/guest")
public class GuestController {

    private GuestService guestService;

    public GuestController(GuestService guestService) {
        this.guestService = guestService;
    }

    @PostMapping
    public ResponseEntity<?> registerGuest(@RequestBody GuestDto guestDto){
        return RestResponseDto.success(guestService.save(guestDto));
    }

    public Guest editGuest(Guest guest){
        return null;
    }

    public List<Guest> listAllGuest(){
        return null;
    }
}
