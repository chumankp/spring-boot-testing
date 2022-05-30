package com.chuman.integration;

import com.chuman.model.Employee;
import com.chuman.repository.EmployeeRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class EmployeeControllerIntegrationTests {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup(){
        employeeRepository.deleteAll();
    }

    @Test
    public void givenEmployeeObject_whenCreateEmployee_thenReturnSavedEmployee() throws Exception {
        //given
        Employee employee = Employee.builder()
                .firstName("chuman")
                .lastName("panda")
                .email("chuman.panda@gmail.com").build();
        //when
        ResultActions responce = mockMvc.perform(post("/api/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employee)));
        //then
        responce.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName",
                        CoreMatchers.is(employee.getFirstName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName",
                        CoreMatchers.is(employee.getLastName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email",
                        CoreMatchers.is(employee.getEmail())));

    }

    @Test
    public void givenListOfEmployee_whenGetAllEmployee_thenReturnEmployeesList() throws Exception{
        //given
        List<Employee> employeeList = new ArrayList<>();
        employeeList.add(Employee.builder().firstName("chuman").lastName("panda").email("chuman@gmail.com").build());
        employeeList.add(Employee.builder().firstName("kalia").lastName("kumar").email("kalia@gmail.com").build());

        employeeRepository.saveAll(employeeList);
        //when
        ResultActions responce = mockMvc.perform(get("/api/employees"));
        //then
        responce.andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()",
                        CoreMatchers.is(employeeList.size())));
    }

    //+ve use case test
    @Test
    public void givenEmployeeId_whenGetEmployeeById_thenReturnEmployeesObject() throws Exception{
        //given
        Employee employee = Employee.builder().firstName("chuman")
                .lastName("panda")
                .email("chuman.panda@gmail.com")
                .build();
        employeeRepository.save(employee);
        //when
        ResultActions responce = mockMvc.perform(get("/api/employees/{id}",employee.getId()));

        //then
        responce.andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName",
                        CoreMatchers.is(employee.getFirstName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName",
                        CoreMatchers.is(employee.getLastName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email",
                        CoreMatchers.is(employee.getEmail())));
    }

    //-ve use case test
    @Test
    public void givenInvalidEmployeeId_whenGetEmployeeById_thenReturnEmpty() throws Exception{
        //given
        long employeeId=1L;
        Employee employee = Employee.builder().firstName("chuman")
                .lastName("panda")
                .email("chuman.panda@gmail.com")
                .build();
        employeeRepository.save(employee);
        //when
        ResultActions responce = mockMvc.perform(get("/api/employees/{id}",employeeId));

        //then
        responce.andExpect(MockMvcResultMatchers.status().isNotFound())
                .andDo(MockMvcResultHandlers.print());

    }
    //update method test +ve use case
    @Test
    public void givenUpdatedEmployee_whenUpdatedEmployee_thenReturnUpdatedEmployeeObject() throws Exception{
        //given
        Employee savedEmployee = Employee.builder()
                .firstName("chuman")
                .lastName("panda")
                .email("chuman.panda@gmail.com").build();
        employeeRepository.save(savedEmployee);

        Employee updateEmployee = Employee.builder()
                .firstName("kalia")
                .lastName("kumar")
                .email("kaliakumar@gmail.com").build();
        //when
        ResultActions responce = mockMvc.perform(put("/api/employees/{id}",savedEmployee.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateEmployee)));

        //then
        responce.andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName",
                        CoreMatchers.is(updateEmployee.getFirstName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName",
                        CoreMatchers.is(updateEmployee.getLastName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email",
                        CoreMatchers.is(updateEmployee.getEmail())));;

    }

    //-ve use case test for update method
    @Test
    public void givenUpdatedEmployee_whenUpdatedEmployee_thenReturnEmpty() throws Exception{
        //given
        long employeeId=1L;
        Employee savedEmployee = Employee.builder()
                .firstName("chuman")
                .lastName("panda")
                .email("chuman.panda@gmail.com").build();
        employeeRepository.save(savedEmployee);

        Employee updateEmployee = Employee.builder()
                .firstName("kalia")
                .lastName("kumar")
                .email("kaliakumar@gmail.com").build();
        //when
        ResultActions responce = mockMvc.perform(put("/api/employees/{id}",employeeId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateEmployee)));

        //then
        responce.andExpect(MockMvcResultMatchers.status().isNotFound())
                .andDo(MockMvcResultHandlers.print());

    }

    // JUnit test for delete employee REST API
    @Test
    public void givenEmployeeId_whenDeleteEmployee_thenReturn() throws Exception {
        // given - precondition or setup
        Employee employee = Employee.builder()
                .firstName("chuman")
                .lastName("panda")
                .email("chuman.panda@gmail.com").build();
        employeeRepository.save(employee);
        //when
        ResultActions responce = mockMvc.perform(delete("/api/employees/{id}",employee.getId()));
        //then
        responce.andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }
}
