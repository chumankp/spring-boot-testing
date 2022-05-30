package com.chuman.service;

import com.chuman.model.EmployeeDTO;

import java.util.List;
import java.util.Optional;

public interface EmployeeService {

    EmployeeDTO saveEmployee(EmployeeDTO employees);

    List<EmployeeDTO> getAllEmployee();

    Optional<EmployeeDTO> getEmployeeById(long id);

    EmployeeDTO updateEmployee(EmployeeDTO updateEmployees);

    void deleteEmployee(long id);
}
