package com.cqu.hqs.dto;

import lombok.Data;

import java.util.List;

@Data
public class RoomCombination {
    private List<RoomResponseDto> rooms;
    private double totalPrice;
}
