package com.cqu.hqs.service;

import com.cqu.hqs.Exception.BadRequestException;
import com.cqu.hqs.Exception.InternalServerErrorException;
import com.cqu.hqs.Exception.ResourceNotFoundException;
import com.cqu.hqs.Repository.EmployeeRepository;
import com.cqu.hqs.dto.EmployeeDto;
import com.cqu.hqs.dto.EmployeeEditDto;
import com.cqu.hqs.dto.EmployeeResponseDto;
import com.cqu.hqs.entity.Employee;
import com.cqu.hqs.entity.User;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.data.domain.Sort;

@Service
public class EmployeeService {

    private UserService userService;
    private ModelMapper mapper;
    private EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository, UserService userService, ModelMapper modelMapper) {
        this.employeeRepository = employeeRepository;
        this.userService = userService;
        this.mapper = modelMapper;
    }

    @Transactional
    public EmployeeDto saveEmployee(EmployeeDto employeeDto) {

        User user = userService.createUser(employeeDto);
        if (employeeRepository.existsByEmail(employeeDto.getEmail())) {
            throw new BadRequestException(("Employee with email " + employeeDto.getEmail() + " already exist"));
        }
        Employee employee = mapToEntity(employeeDto);
        employee.setCreatedDate(LocalDateTime.now());
        employee.setUser(user);
        employee = employeeRepository.save(employee);
        if (null == employee) {
            throw new InternalServerErrorException("Error while saving employee");
        }
        return mapToDto(employee);
    }

    public List<EmployeeResponseDto> getAllEmployees() {
        List<Employee> employees = employeeRepository.findAll(Sort.by(Sort.Direction.DESC, "createdDate"));
        return employees.stream().filter(employee -> employee.getStatus().equals("ACTIVE")).map(employee -> mapToResponseDto(employee)).collect((Collectors.toList()));
    }

    private EmployeeDto mapToDto(Employee employee) {
        EmployeeDto employeeDto = mapper.map(employee, EmployeeDto.class);
        return employeeDto;
    }

    public EmployeeEditDto mapToEditDto(Employee employee) {
        EmployeeEditDto employeeEditDto = mapper.map(employee, EmployeeEditDto.class);
        return employeeEditDto;
    }

    private Employee mapToEntity(EmployeeDto employeeDto) {
        Employee employee = mapper.map(employeeDto, Employee.class);
        return employee;
    }

    public EmployeeResponseDto mapToResponseDto(Employee employee) {
        EmployeeResponseDto employeeResponseDto = mapper.map(employee, EmployeeResponseDto.class);
        return employeeResponseDto;
    }


    public EmployeeEditDto editEmployee(EmployeeEditDto empEditDto) {
        Employee employee = employeeRepository.findById(empEditDto.getId()).orElseThrow(() -> new ResourceNotFoundException("Employee with id " + empEditDto.getId() + " not found."));
        if (empEditDto.getEmail() != null) {
            if (employeeRepository.existsByEmail(empEditDto.getEmail())) {
                throw new BadRequestException("Email Already Exists.");
            } else {
                employee.setEmail(empEditDto.getEmail());
            }
        }
        if (empEditDto.getFirstName() != null)
            employee.setFirstName(empEditDto.getFirstName());
        if (empEditDto.getLastName() != null)
            employee.setLastName(empEditDto.getLastName());
        if (empEditDto.getAddress() != null)
            employee.setAddress(empEditDto.getAddress());
        if (empEditDto.getCity() != null)
            employee.setCity(empEditDto.getCity());
        if (empEditDto.getState() != null)
            employee.setState(empEditDto.getState());
        if (empEditDto.getCountry() != null)
            employee.setCountry(empEditDto.getCountry());
        if (empEditDto.getPostalCode() != null)
            employee.setPostalCode(empEditDto.getPostalCode());
        if (empEditDto.getPhone() != null)
            employee.setPhone(empEditDto.getPhone());
        if (empEditDto.getPosition() != null)
            employee.setPosition(empEditDto.getPosition());
         if (empEditDto.getSalary()!=0)
            employee.setSalary(empEditDto.getSalary());
        if (empEditDto.getHireDate() != null)
            employee.setHireDate(empEditDto.getHireDate());
        if (empEditDto.getStatus() != null)
            employee.setStatus(empEditDto.getStatus());
        employee.setUpdatedDate(LocalDateTime.now());
        employee=employeeRepository.save(employee);
        return mapToEditDto(employee);
    }

    public String deleteEmployeeWithId(Long id) {
        Employee employee = employeeRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Employee with id " + id + " not found."));
        employee.setStatus("INACTIVE");
        employeeRepository.save(employee);
        return "Deleted Successfully";
    }

    public EmployeeResponseDto getEmployeeById(Long id) {
        Employee employee = employeeRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Employee with id " + id + " not found."));
        return mapToResponseDto(employee);
    }
}
