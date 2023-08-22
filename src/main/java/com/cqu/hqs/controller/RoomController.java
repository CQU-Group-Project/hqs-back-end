package com.cqu.hqs.controller;


import com.cqu.hqs.entity.Room;
import com.cqu.hqs.utils.RoomStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/room")
public class RoomController {

    private Room room;

    private RoomController(Room room){
        this.room=room;
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
