
package com.cqu.hqs.dto;

import java.time.LocalDate;
import lombok.Data;

@Data
public class RevenueResponse {
    
    private LocalDate date;
    private double amount;
    
}
