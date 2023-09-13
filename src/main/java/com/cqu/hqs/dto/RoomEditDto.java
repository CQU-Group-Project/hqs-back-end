package com.cqu.hqs.dto;

import lombok.Data;

@Data
public class RoomEditDto {

    private Long id;
    private int roomNumber;
    private boolean isAvailable;
    private boolean isClean;
    private double price;
    private RoomTypeDto roomTypeDto;
}
