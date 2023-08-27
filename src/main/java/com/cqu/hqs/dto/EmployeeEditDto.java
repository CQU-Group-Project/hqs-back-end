package com.cqu.hqs.dto;

import lombok.Data;

@Data
public class EmployeeEditDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String address;
    private String city;
    private String state;
    private String country;
    private String postalCode;
    private String phone;
    private String email;
    private String position;
    private String hireDate;
    private double salary;
    private String status;
}
