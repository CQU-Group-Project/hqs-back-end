package com.cqu.hqs.controller;


import com.cqu.hqs.dto.RoomDto;
import com.cqu.hqs.dto.RoomEditDto;
import com.cqu.hqs.entity.Room;
import com.cqu.hqs.service.RoomService;
import com.cqu.hqs.utils.RestResponseDto;
import com.cqu.hqs.utils.RoomStatus;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/room")
public class RoomController {

    private RoomService roomService;

    public RoomController(RoomService roomService){
        this.roomService=roomService;
    }

    @PostMapping
    private ResponseEntity<?> saveRoom(@RequestBody RoomDto roomDto){
        return RestResponseDto.success(roomService.saveRoom(roomDto));
    }
    
    @PutMapping
    private ResponseEntity<?> editRoom(@RequestBody RoomEditDto roomEditDto){
        return RestResponseDto.success(roomService.editRoom(roomEditDto));
    }

    @GetMapping
    private ResponseEntity<?> listAllAvailableRoom(){

        return RestResponseDto.success(roomService.getAllRooms());
    }
}
