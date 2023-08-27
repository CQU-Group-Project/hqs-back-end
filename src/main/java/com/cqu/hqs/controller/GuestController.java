package com.cqu.hqs.controller;

import com.cqu.hqs.dto.GuestDto;
import com.cqu.hqs.dto.GuestEditDto;
import com.cqu.hqs.entity.Employee;
import com.cqu.hqs.entity.Guest;
import com.cqu.hqs.entity.User;
import com.cqu.hqs.service.GuestService;
import com.cqu.hqs.service.UserService;
import com.cqu.hqs.utils.RestResponseDto;
import org.apache.coyote.Response;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import org.springframework.http.ResponseEntity;

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


    @GetMapping
    public ResponseEntity<?> getAllGuests(){
        return RestResponseDto.success(guestService.getAllGuests());
    }

    @PutMapping
    public ResponseEntity<?> editGuest(@RequestBody GuestEditDto guestEditDto){
        return RestResponseDto.success(guestService.editGuest(guestEditDto));
    }

    public Guest editGuest(Guest guest){
        return null;
    }

    public List<Guest> listAllGuest(){
        return null;
    }
}
