package com.cqu.hqs.dto;

import lombok.Data;

@Data
public class RoomDto {
    private Long id;
    private int roomNumber;
    private boolean isAvailable;
    private boolean isClean;
    private double price;
    private String description;
    private RoomTypeDto roomTypeDto;

}
