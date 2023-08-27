package com.cqu.hqs.controller;


import com.cqu.hqs.dto.RoomDto;
import com.cqu.hqs.entity.Room;
import com.cqu.hqs.service.RoomService;
import com.cqu.hqs.utils.RestResponseDto;
import com.cqu.hqs.utils.RoomStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/room")
public class RoomController {

    private RoomService roomService;

    public RoomController(RoomService roomService){
        this.roomService=roomService;
    }

    @PostMapping
    private ResponseEntity<?> RoomController(@RequestBody RoomDto roomDto){
        return RestResponseDto.success(roomService.saveRoom(roomDto));
    }

    private void addRoom(Room room){

    }

    private void editRoom(Room room){

    }

    private void changeRoomStatus(RoomStatus roomStatus){

    }

    private List<Room> listAllAvailableRoom(){
        return null;
    }
}
