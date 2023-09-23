package com.cqu.hqs.controller;

import com.cqu.hqs.dto.EmployeeDto;
import com.cqu.hqs.dto.QueryDto;
import com.cqu.hqs.service.QueryService;
import com.cqu.hqs.utils.RestResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/query")
public class QueryController {

    private QueryService queryService;

    public QueryController(QueryService queryService) {
        this.queryService = queryService;
    }

    @PostMapping
    public ResponseEntity<?> saveQuery(@RequestBody QueryDto queryDto) {
        return RestResponseDto.success(queryService.saveQuery(queryDto));
    }

    @GetMapping
    public ResponseEntity<?> getAllQuery() {
        return RestResponseDto.success(queryService.getAllQueries());
    }

}
