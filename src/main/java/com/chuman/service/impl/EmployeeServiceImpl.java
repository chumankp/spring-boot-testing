package com.chuman.service.impl;

import com.chuman.exception.ResourceNotFoundException;
import com.chuman.model.EmployeeDTO;
import com.chuman.repository.EmployeeRepository;
import com.chuman.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private EmployeeRepository employeeRepository;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Override
    public EmployeeDTO saveEmployee(EmployeeDTO employees) {
        Optional<EmployeeDTO> savedEmployee = employeeRepository.findByEmail(employees.getEmail());
        if(savedEmployee.isPresent()){
            throw new ResourceNotFoundException("Employee already exit with given email "+employees.getEmail());
        }
        return employeeRepository.save(employees);
    }

    @Override
    public List<EmployeeDTO> getAllEmployee() {
        return employeeRepository.findAll();
    }

    @Override
    public Optional<EmployeeDTO> getEmployeeById(long id) {
        return employeeRepository.findById(id);
    }

    @Override
    public EmployeeDTO updateEmployee(EmployeeDTO updateEmployees) {
        return employeeRepository.save(updateEmployees);
    }

    @Override
    public void deleteEmployee(long id) {
        employeeRepository.deleteById(id);
    }
}
