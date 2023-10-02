package com.cqu.hqs.controller;

import com.cqu.hqs.dto.EmployeeDto;
import com.cqu.hqs.dto.EmployeeEditDto;
import com.cqu.hqs.dto.GuestEditDto;
import com.cqu.hqs.entity.Employee;
import com.cqu.hqs.service.EmployeeService;
import com.cqu.hqs.utils.RestResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController("Employee Controller")
@RequestMapping("/api/employee")
public class EmployeeController {

    private EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @PostMapping
    public ResponseEntity<?> addEmployee(@RequestBody EmployeeDto employeeDto) {
        return RestResponseDto.success(employeeService.saveEmployee(employeeDto));
    }

    @GetMapping
    public ResponseEntity<?> getAllEmployees() {
        return RestResponseDto.success(employeeService.getAllEmployees());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getAllEmployeeById(@PathVariable Long id) {
        return RestResponseDto.success(employeeService.getEmployeeById(id));
    }

    @PutMapping
    public ResponseEntity<?> editGuest(@RequestBody EmployeeEditDto empEditDto) {
        return RestResponseDto.success(employeeService.editEmployee(empEditDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteEmployee(@PathVariable Long id) {
        return RestResponseDto.success(employeeService.deleteEmployeeWithId(id));
    }

    public Employee editEmployee(Employee employee) {
        return null;
    }

    public Employee getEmployeeById(Long id) {
        return null;
    }

    public List<Employee> listAllEmployee() {
        return null;
    }
}
