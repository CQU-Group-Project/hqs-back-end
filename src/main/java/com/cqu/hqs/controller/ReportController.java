package com.cqu.hqs.controller;

import com.cqu.hqs.service.ReportService;
import com.cqu.hqs.utils.RestResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/report")
public class ReportController {

    private ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping
    public ResponseEntity<?> getAllBookings() {
        return RestResponseDto.success(reportService.getReport());
    }

}
