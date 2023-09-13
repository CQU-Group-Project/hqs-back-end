/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cqu.hqs.dto;

import com.cqu.hqs.entity.Guest;
import java.time.LocalDate;
import java.util.List;
import lombok.Data;

/**
 *
 * @author Deependra
 */

@Data
public class BookingDto {
    
    private List<Long> roomId;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private double totalAmount;
    private Long guestId;
    
    
}
