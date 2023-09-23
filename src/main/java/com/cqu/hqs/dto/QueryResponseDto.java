package com.cqu.hqs.dto;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class QueryResponseDto {

    private Long id;
    private String fullname;
    private String email;
    private String message;
    private String phoneNo;
    private LocalDateTime createdDate;
}
