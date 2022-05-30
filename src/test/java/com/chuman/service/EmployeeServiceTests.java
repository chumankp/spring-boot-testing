package com.chuman.service;

import com.chuman.exception.ResourceNotFoundException;
import com.chuman.model.EmployeeDTO;
import com.chuman.repository.EmployeeRepository;
import com.chuman.service.impl.EmployeeServiceImpl;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
 class EmployeeServiceTests {
    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    private EmployeeDTO employee;

    @BeforeEach
    public void setup(){
employeeRepository = Mockito.mock(EmployeeRepository.class);
        employeeService = new EmployeeServiceImpl(employeeRepository);

         employee = EmployeeDTO.builder().id(1L)
                .firstName("chuman")
                .lastName("panda")
                .email("chuman.panda@gmail.com")
                .build();
    }
    //svae employee method
    @Test
     void givenEmployeeObject_whenSave_thenReturnSavedEmployee(){
        //given or setup
        given(employeeRepository.findByEmail(employee.getEmail()))
                .willReturn(Optional.empty());
        given(employeeRepository.save(employee)).willReturn(employee);
        //when action
        EmployeeDTO savedEmployee = employeeService.saveEmployee(employee);
        //then verify the output
        assertThat(savedEmployee).isNotNull();
    }
    //svae employee method with exception
    @Test
     void givenEmail_whenSave_thenThrowsException(){
        //given or setup
        given(employeeRepository.findByEmail(employee.getEmail()))
                .willReturn(Optional.of(employee));
        //when action
        org.junit.jupiter.api.Assertions.assertThrows(ResourceNotFoundException.class, ()->{employeeService.saveEmployee(employee);});
        //then
        verify(employeeRepository, never()).save(any(EmployeeDTO.class));
    }

    //get all employee method
    @Test
    public void givenEmployeeList_whenGetAllEmployee_thenReturnAllEmployeeList(){
        //given or setup
        EmployeeDTO employee1 = EmployeeDTO.builder().id(2L)
                .firstName("Kalia")
                .lastName("kumar")
                .email("kalia.kumar@gmail.com")
                .build();
        given(employeeRepository.findAll()).willReturn(List.of(employee, employee1));
        //when action
        List<EmployeeDTO> employeeList = employeeService.getAllEmployee();
        //then
       assertThat(employeeList).isNotNull();
       assertThat(employeeList.size()).isEqualTo(2);
    }

    //get all employee method -ve test
    @Test
    public void givenEmployeeEmptyList_whenGetAllEmployee_thenReturnEmptyEmployeeList(){
        //given or setup
        EmployeeDTO employee1 = EmployeeDTO.builder().id(2L)
                .firstName("Kalia")
                .lastName("kumar")
                .email("kalia.kumar@gmail.com")
                .build();
        given(employeeRepository.findAll()).willReturn(Collections.emptyList());
        //when action
        List<EmployeeDTO> employeeList = employeeService.getAllEmployee();
        //then
        assertThat(employeeList).isEmpty();
        assertThat(employeeList.size()).isEqualTo(0);
    }

    //get  employee by id method
    @Test
    public void givenEmployeeId_whenGetEmployeeById_thenReturnEmployeeObject(){
        //given or setup
        given(employeeRepository.findById(1L)).willReturn(Optional.of(employee));
        //when action
        EmployeeDTO savedEmployee = employeeService.getEmployeeById(employee.getId()).get();
        //then
        assertThat(savedEmployee).isNotNull();
    }
    //update employee method
    @Test
    public void givenEmployeeObject_whenUpdateEmployee_thenReturnUpdateedEmployee(){
        //given or setup
        given(employeeRepository.save(employee)).willReturn(employee);
        employee.setEmail("kalia.kumar@gmail.com");
        employee.setFirstName("kalia");
        //when action
        EmployeeDTO updateEmployee = employeeService.updateEmployee(employee);
        //then
        assertThat(updateEmployee.getEmail()).isEqualTo("kalia.kumar@gmail.com");
        assertThat(updateEmployee.getFirstName()).isEqualTo("kalia");
    }

    //delete employee method
    @Test
    public void givenEmployeeId_whenDeleteEmployee_thenNothing(){
        //given or setup
        long employeeId=1L;
        willDoNothing().given(employeeRepository).deleteById(employeeId);

        //when action
         employeeService.deleteEmployee(employeeId);
        //then
       verify(employeeRepository, times(1)).deleteById(employeeId);
    }
}
