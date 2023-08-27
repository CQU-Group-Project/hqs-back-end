package com.cqu.hqs.dto;

import lombok.Data;

@Data
public class RoomTypeDto {
    private Long id;
    private String name;
    private String description;
    private int maxOccupancy;
    private String amentities;
}
