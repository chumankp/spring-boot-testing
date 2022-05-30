package com.chuman.controller;

import com.chuman.model.EmployeeDTO;
import com.chuman.service.EmployeeService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    private static final Log LOGGER = LogFactory.getLog(EmployeeController.class);

    private final EmployeeService employeeService;

    private EmployeeController(EmployeeService employeeService) {

        this.employeeService = employeeService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EmployeeDTO createEmployee(@RequestBody EmployeeDTO employee){
        return employeeService.saveEmployee(employee);
    }

    @GetMapping
    public List<EmployeeDTO> getAllEmployees(){
        LOGGER.info("get all employee data " + employeeService.getAllEmployee());
        return employeeService.getAllEmployee();
    }
    @GetMapping("{id}")
    public ResponseEntity<EmployeeDTO> getEmployeeById(@PathVariable("id") long employeeId){
    return employeeService.getEmployeeById(employeeId).map(ResponseEntity::ok)
            .orElseGet(()-> ResponseEntity.notFound().build());
    }
    @PutMapping("{id}")
    public ResponseEntity<EmployeeDTO> updateEmployee(@PathVariable("id") long employeeId, @RequestBody EmployeeDTO employee){
       LOGGER.info("Employee id not found :- {}");
        return employeeService.getEmployeeById(employeeId)
                .map(savedEmployee ->{
                    savedEmployee.setFirstName(employee.getFirstName());
                    savedEmployee.setLastName(employee.getLastName());
                    savedEmployee.setEmail(employee.getEmail());
                    EmployeeDTO updateEmployee = employeeService.updateEmployee(savedEmployee);
                    return new ResponseEntity<>(updateEmployee, HttpStatus.OK);
                }).orElseGet(()->ResponseEntity.notFound().build());
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteEmployee(@PathVariable("id") long employeeId){
         employeeService.deleteEmployee(employeeId);
         return new ResponseEntity<>("Employee deleted Successfully",HttpStatus.OK);
    }
}
