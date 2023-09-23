
package com.cqu.hqs.dto;

import java.time.LocalDate;
import java.util.Map;
import lombok.Data;

@Data
public class ReportResponseDto {
    
    private double roomOccupancy;
    private int totalBookings;
    private double totalRevenue;
    private int totalEmployee;
    private int totalGuests;
    private Map<LocalDate, Double> revenueResponse;
    
}
