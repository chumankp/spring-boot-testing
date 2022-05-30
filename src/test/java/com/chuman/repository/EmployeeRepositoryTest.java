package com.chuman.repository;

import com.chuman.model.Employee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
 class EmployeeRepositoryTest {
    @Autowired
    private EmployeeRepository employeeRepository;

    @BeforeEach
    public void setup(){
        employeeRepository.deleteAll();
    }



    @Test
     void givenEmployeeObject_whenSave_thenReturnSavedEmployee(){
        //given or setup
        
        Employee employee = Employee.builder().firstName("chuman")
                            .lastName("panda")
                            .email("chuman.panda@gmail.com")
                            .build();

        //when action
        Employee savedEmployee = employeeRepository.save(employee);

        //then verify the output
        assertThat(savedEmployee).isNotNull();
        assertThat(savedEmployee.getId()).isPositive();
    }

    @Test
     void givenEmployeeList_whenFindAll_thenEmployeeList(){
        //given or setup
        Employee employee = Employee.builder().firstName("chuman")
                .lastName("panda")
                .email("chuman.panda@gmail.com")
                .build();
        Employee employee1 = Employee.builder().firstName("kalia")
                .lastName("panda")
                .email("kalia.panda@gmail.com")
                .build();

        employeeRepository.save(employee);
        employeeRepository.save(employee1);

        //when action
        List<Employee> employeeList = employeeRepository.findAll();

        //then verify the output
        assertThat(employeeList).isNotNull();
        assertThat(employeeList.size()).isEqualTo(2);
    }

    @Test
     void givenEmployeeObject_whenFindById_thenReturnEmployeeObject(){
        //given or setup
        Employee employee = Employee.builder().firstName("chuman")
                .lastName("panda")
                .email("chuman.panda@gmail.com")
                .build();
        employeeRepository.save(employee);

        //when action
        Employee employeeData = employeeRepository.findById(employee.getId()).get();

        //then verify the output
        assertThat(employeeData).isNotNull();
    }
    //junit for test employee by email address
    @Test
     void givenEmployeeEmail_whenFindByEmail_thenReturnEmployeeObject(){
        //given or setup
        Employee employee = Employee.builder().firstName("chuman")
                .lastName("panda")
                .email("chuman.panda@gmail.com")
                .build();
        employeeRepository.save(employee);

        //when action
        Employee employeeData = employeeRepository.findByEmail(employee.getEmail()).get();
        //then verify the output
        assertThat(employeeData).isNotNull();
    }
    //Update method
    @Test
     void givenEmployeeObject_whenUpdateEmployee_thenReturnUpdateEmployee(){
        //given or setup
        Employee employee = Employee.builder().firstName("chuman")
                .lastName("panda")
                .email("chuman.panda@gmail.com")
                .build();
        employeeRepository.save(employee);
        //when action
        Employee employeeData = employeeRepository.findById(employee.getId()).get();
        employeeData.setEmail("kumar@gmail.com");
        employeeData.setFirstName("kalia");
        Employee updateEmployee = employeeRepository.save(employeeData);
        //then verify the output
        assertThat(updateEmployee.getEmail()).isEqualTo("kumar@gmail.com");
        assertThat(updateEmployee.getFirstName()).isEqualTo("kalia");
    }

    //delete method
    @Test
     void givenEmployeeObject_whenDeleteEmployee_thenRemoveEmployee(){
        //given or setup
        Employee employee = Employee.builder().firstName("chuman")
                .lastName("panda")
                .email("chuman.panda@gmail.com")
                .build();
        employeeRepository.save(employee);
        //when action
        employeeRepository.delete(employee);

        Optional<Employee> optionalEmployee = employeeRepository.findById(employee.getId());
        //then verify the output
        assertThat(optionalEmployee).isEmpty();
    }

    //custom query useing jpql with index
    @Test
     void givenFirstNameAndLastName_whenFindByQuery_thenReturnEmployeeObject(){
        //given or setup
        Employee employee = Employee.builder().firstName("chuman")
                .lastName("panda")
                .email("chuman.panda@gmail.com")
                .build();
        employeeRepository.save(employee);
        //when action
        Employee employeeData = employeeRepository.findByCustomQuery(employee.getFirstName(),employee.getLastName());
        //then verify the output
        assertThat(employeeData).isNotNull();
    }

    @Test
     void givenFirstNameAndLastName_whenFindByNamedParams_thenReturnEmployeeObject(){
        //given or setup
        Employee employee = Employee.builder().firstName("chuman")
                .lastName("panda")
                .email("chuman.panda@gmail.com")
                .build();
        employeeRepository.save(employee);
        //when action
        Employee employeeData = employeeRepository.findByCustomQueryNamedParams(employee.getFirstName(),employee.getLastName());
        //then verify the output
        assertThat(employeeData).isNotNull();
    }

    @Test
    void givenFirstNameAndLastName_whenFindByNativeSql_thenReturnEmployeeObject(){
        //given or setup
        Employee employee = Employee.builder().firstName("chuman")
                .lastName("panda")
                .email("chuman.panda@gmail.com")
                .build();
        employeeRepository.save(employee);
        //when action
        Employee employeeData = employeeRepository.findByCustomNativeSql(employee.getFirstName(),employee.getLastName());
        //then verify the output
        assertThat(employeeData).isNotNull();
    }

    @Test
     void givenFirstNameAndLastName_whenFindByNativeSqlNamedParams_thenReturnEmployeeObject(){
        //given or setup
        Employee employee = Employee.builder().firstName("chuman")
                .lastName("panda")
                .email("chuman.panda@gmail.com")
                .build();
        employeeRepository.save(employee);
        //when action
        Employee employeeData = employeeRepository.findByCustomNativeSqlNamedParams(employee.getFirstName(),employee.getLastName());
        //then verify the output
        assertThat(employeeData).isNotNull();
    }
}

