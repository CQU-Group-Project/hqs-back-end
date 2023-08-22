package com.cqu.hqs.controller;

import com.cqu.hqs.entity.Employee;
import com.cqu.hqs.entity.Guest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/employee")
public class EmployeeController {

    private Employee employee;

    public EmployeeController(Employee employee) {
        this.employee = employee;
    }


    public Guest addEmployee(Employee employee){
        return null;
    }

    public Employee editEmployee(Employee employee){
        return null;
    }

    public Employee getEmployeeById(Long id){
        return null;
    }

    public List<Employee> listAllEmployee(){
        return null;
    }
}
