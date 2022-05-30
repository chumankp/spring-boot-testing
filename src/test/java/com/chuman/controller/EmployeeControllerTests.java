package com.chuman.controller;


import com.chuman.model.EmployeeDTO;
import com.chuman.service.EmployeeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
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

@WebMvcTest
public class EmployeeControllerTests {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private EmployeeService employeeService;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
     void givenEmployeeObject_whenCreateEmployee_thenReturnSavedEmployee() throws Exception {
        //given
        EmployeeDTO employee = EmployeeDTO.builder()
                .firstName("chuman")
                .lastName("panda")
                .email("chuman.panda@gmail.com").build();
        given(employeeService.saveEmployee(ArgumentMatchers.any(EmployeeDTO.class)))
                .willAnswer((invocation) -> invocation.getArgument(0));
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
     void givenListOfEmployee_whenGetAllEmployee_thenReturnEmployeesList() throws Exception{
        //given
        List<EmployeeDTO> employeeList = new ArrayList<>();
        employeeList.add(EmployeeDTO.builder().firstName("chuman").lastName("panda").email("chuman@gmail.com").build());
        employeeList.add(EmployeeDTO.builder().firstName("kalia").lastName("kumar").email("kalia@gmail.com").build());

        given(employeeService.getAllEmployee()).willReturn(employeeList);
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
     void givenEmployeeId_whenGetEmployeeById_thenReturnEmployeesObject() throws Exception{
        //given
        long employeeId=1L;
        EmployeeDTO employee = EmployeeDTO.builder()
                .firstName("chuman")
                .lastName("panda")
                .email("chuman.panda@gmail.com").build();
        given(employeeService.getEmployeeById(employeeId)).willReturn(Optional.of(employee));
        //when
        ResultActions responce = mockMvc.perform(get("/api/employees/{id}",employeeId));

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
     void givenInvalidEmployeeId_whenGetEmployeeById_thenReturnEmpty() throws Exception{
        //given
        long employeeId=1L;
        EmployeeDTO employee = EmployeeDTO.builder()
                .firstName("chuman")
                .lastName("panda")
                .email("chuman.panda@gmail.com").build();
        given(employeeService.getEmployeeById(employeeId)).willReturn(Optional.empty());
        //when
        ResultActions responce = mockMvc.perform(get("/api/employees/{id}",employeeId));

        //then
        responce.andExpect(MockMvcResultMatchers.status().isNotFound())
                .andDo(MockMvcResultHandlers.print());

    }

    //update method test +ve use case
    @Test
     void givenUpdatedEmployee_whenUpdatedEmployee_thenReturnUpdatedEmployeeObject() throws Exception{
        //given
        long employeeId=1L;
        EmployeeDTO savedEmployee = EmployeeDTO.builder()
                .firstName("chuman")
                .lastName("panda")
                .email("chuman.panda@gmail.com").build();

        EmployeeDTO updateEmployee = EmployeeDTO.builder()
                .firstName("kalia")
                .lastName("kumar")
                .email("kaliakumar@gmail.com").build();

        given(employeeService.getEmployeeById(employeeId)).willReturn(Optional.of(savedEmployee));

        given(employeeService.updateEmployee(ArgumentMatchers.any(EmployeeDTO.class)))
                .willAnswer((invocation) -> invocation.getArgument(0));
        //when
        ResultActions responce = mockMvc.perform(put("/api/employees/{id}",employeeId)
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
     void givenUpdatedEmployee_whenUpdatedEmployee_thenReturnEmpty() throws Exception{
        //given
        long employeeId=1L;
        EmployeeDTO savedEmployee = EmployeeDTO.builder()
                .firstName("chuman")
                .lastName("panda")
                .email("chuman.panda@gmail.com").build();

        EmployeeDTO updateEmployee = EmployeeDTO.builder()
                .firstName("kalia")
                .lastName("kumar")
                .email("kaliakumar@gmail.com").build();

        given(employeeService.getEmployeeById(employeeId)).willReturn(Optional.empty());

        given(employeeService.updateEmployee(ArgumentMatchers.any(EmployeeDTO.class)))
                .willAnswer((invocation) -> invocation.getArgument(0));
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
     void givenEmployeeId_whenDeleteEmployee_thenReturn() throws Exception {
        // given - precondition or setup
        long employeeId=1L;
        willDoNothing().given(employeeService).deleteEmployee(employeeId);
        //when
        ResultActions responce = mockMvc.perform(delete("/api/employees/{id}",employeeId));
        //then
        responce.andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }
}
