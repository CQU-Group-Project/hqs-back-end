package com.cqu.hqs.service;

import com.cqu.hqs.Exception.BadRequestException;
import com.cqu.hqs.Repository.RoomRepository;
import com.cqu.hqs.Repository.RoomTypeRepository;
import com.cqu.hqs.dto.RoomDto;
import com.cqu.hqs.dto.RoomTypeDto;
import com.cqu.hqs.entity.Room;
import com.cqu.hqs.entity.RoomType;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class RoomService {

    private ModelMapper mapper;
    private RoomRepository roomRepository;
    private RoomTypeRepository roomTypeRepository;


    public RoomService(RoomRepository roomRepository, RoomTypeRepository roomTypeRepository, ModelMapper mapper) {
        this.mapper = mapper;
        this.roomRepository = roomRepository;
        this.roomTypeRepository = roomTypeRepository;
    }

    @Transactional
    public RoomDto saveRoom(RoomDto roomDto) {
        if (roomRepository.existsByRoomNumber(roomDto.getRoomNumber())) {
            throw new BadRequestException("Room already exists with room number " + roomDto.getRoomNumber());
        }
        Room room = mapToEntity(roomDto);

        RoomType roomType = roomTypeRepository.findByName(roomDto.getRoomTypeDto().getName());
        if (null == roomType){
            roomType = mapToRoomTypeEntity(roomDto.getRoomTypeDto());
            roomType = roomTypeRepository.save(roomType);
        }


        room.setRoomType(roomType);
        room.setCreatedDate(LocalDateTime.now());
        room = roomRepository.save(room);

        return mapToDto(room);
    }

    public RoomDto mapToDto(Room room) {
        RoomDto roomDto = mapper.map(room, RoomDto.class);
        roomDto.setRoomTypeDto(mapToRoomTypeDto(room.getRoomType()));
        return roomDto;
    }


    public Room mapToEntity(RoomDto roomDto) {
        Room room = mapper.map(roomDto, Room.class);
        return room;
    }

    public RoomType mapToRoomTypeEntity(RoomTypeDto roomTypeDto) {
        RoomType roomType = mapper.map(roomTypeDto, RoomType.class);
        return roomType;
    }

    public RoomTypeDto mapToRoomTypeDto(RoomType roomType) {
        RoomTypeDto roomTypeDto = mapper.map(roomType, RoomTypeDto.class);
        return roomTypeDto;
    }


}
