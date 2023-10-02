package com.cqu.hqs.service;

import com.cqu.hqs.Exception.BadRequestException;
import com.cqu.hqs.Exception.ResourceNotFoundException;
import com.cqu.hqs.Repository.RoomRepository;
import com.cqu.hqs.Repository.RoomTypeRepository;
import com.cqu.hqs.dto.RoomDto;
import com.cqu.hqs.dto.RoomEditDto;
import com.cqu.hqs.dto.RoomResponseDto;
import com.cqu.hqs.dto.RoomTypeDto;
import com.cqu.hqs.dto.RoomTypeResponseDto;
import com.cqu.hqs.entity.Employee;
import com.cqu.hqs.entity.Room;
import com.cqu.hqs.entity.RoomType;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.data.domain.Sort;

@Service
public class RoomService {

    private ModelMapper mapper;
    private RoomRepository roomRepository;
    private RoomTypeRepository roomTypeRepository;
    private BookingService bookingService;

    public RoomService(RoomRepository roomRepository, RoomTypeRepository roomTypeRepository, ModelMapper mapper, BookingService bookingService) {
        this.mapper = mapper;
        this.roomRepository = roomRepository;
        this.roomTypeRepository = roomTypeRepository;
        this.bookingService = bookingService;
    }

    @Transactional
    public RoomDto saveRoom(RoomDto roomDto) {
        if (roomRepository.existsByRoomNumberAndStatus(roomDto.getRoomNumber(), "ACTIVE")) {
            throw new BadRequestException("Room already exists with room number " + roomDto.getRoomNumber());
        }
        Room room = mapToEntity(roomDto);

        RoomType roomType = roomTypeRepository.findByName(roomDto.getRoomTypeDto().getName());
        if (null == roomType) {
            roomType = mapToRoomTypeEntity(roomDto.getRoomTypeDto());
            roomType = roomTypeRepository.save(roomType);
        }

        room.setRoomType(roomType);
        room.setCreatedDate(LocalDateTime.now());
        room = roomRepository.save(room);

        return mapToDto(room);
    }

    @Transactional
    public RoomEditDto editRoom(RoomEditDto roomEditDto) {

        Room room = roomRepository.findById(roomEditDto.getId()).orElseThrow(() -> new ResourceNotFoundException("Room with id " + roomEditDto.getId() + " not found."));

        //If new room number has been passed
        if (roomEditDto.getRoomNumber() != 0) {
            if (roomRepository.existsByRoomNumberAndStatus(roomEditDto.getRoomNumber(), "ACTIVE")) {
                throw new BadRequestException("Room already exists with room number " + roomEditDto.getRoomNumber());
            }
            room.setRoomNumber(roomEditDto.getRoomNumber());
        }
        room.setPrice(roomEditDto.getPrice());
        room.setAvailable(roomEditDto.isAvailable());
        room.setClean(roomEditDto.isClean());
        room.setDescription(roomEditDto.getDescription());

        RoomType roomType = roomTypeRepository.findByName(roomEditDto.getRoomTypeDto().getName());
        if (null == roomType) {
            roomType = mapToRoomTypeEntity(roomEditDto.getRoomTypeDto());
            roomType = roomTypeRepository.save(roomType);
        }

        room.setRoomType(roomType);
        room.setCreatedDate(LocalDateTime.now());
        room.setUpdatedDate(LocalDateTime.now());
        room = roomRepository.save(room);

        return mapToEditDto(room);
    }

    public String deleteRoomWithId(Long id) {
        Room room = roomRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Room with id " + id + " not found."));
        room.setStatus("INACTIVE");
        room.setUpdatedDate(LocalDateTime.now());
        roomRepository.save(room);
        return "Deleted Successfully";
    }
    
        public RoomResponseDto getRoomById(Long id) {
        Room room = roomRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Room with id " + id + " not found."));
        return mapToResponseDto(room);
    }

    public RoomDto mapToDto(Room room) {
        RoomDto roomDto = mapper.map(room, RoomDto.class);
        roomDto.setRoomTypeDto(mapToRoomTypeDto(room.getRoomType()));
        return roomDto;
    }

    public RoomEditDto mapToEditDto(Room room) {
        RoomEditDto roomEditDto = mapper.map(room, RoomEditDto.class);
        roomEditDto.setRoomTypeDto(mapToRoomTypeDto(room.getRoomType()));
        return roomEditDto;
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

    public List<RoomResponseDto> getAllRooms() {
        List<Room> rooms = roomRepository.findAll(Sort.by(Sort.Direction.DESC, "createdDate"));
        return rooms.stream().filter(room -> room.getStatus().equals("ACTIVE")).map(room -> mapToResponseDto(room)).collect((Collectors.toList()));
    }

    public RoomResponseDto mapToResponseDto(Room room) {
        RoomResponseDto roomResponseDto = mapper.map(room, RoomResponseDto.class);
        return roomResponseDto;
    }

}
