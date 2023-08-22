package com.cqu.hqs.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


@NoArgsConstructor
@AllArgsConstructor
public class SearchBookingDto {
    private Date checkInDate;
    private Date checkoutDate;
    private int roomNo;
    private int guest;

}
