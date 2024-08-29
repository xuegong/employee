package com.example.rqchallenge;

import com.example.rqchallenge.employees.model.Employee;
import com.example.rqchallenge.employees.service.IEmployeeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IEmployeeService employeeService;
    @Autowired
    private ObjectMapper objectMapper;

    private Employee employee1;
    private Employee employee2;

    @BeforeEach
    void setUp() {
        employee1 = new Employee(1, "Will Smith", 12323, 30, "image1.jpg");
        employee2 = new Employee(2, "Bill Gates", 1232132, 25, "image2.jpg");
    }

    @Test
    void testGetAllEmployees() throws Exception {
        List<Employee> employees = Arrays.asList(employee1, employee2);

        when(employeeService.getAllEmployees()).thenReturn(employees);

        mockMvc.perform(get("/employees"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(employee1.getId())))
                .andExpect(jsonPath("$[0].employee_name", is(employee1.getName())))
                .andExpect(jsonPath("$[0].employee_age", is(employee1.getAge())))
                .andExpect(jsonPath("$[0].profile_image", is(employee1.getProfileImage())))
                .andExpect(jsonPath("$[1].id", is(employee2.getId())))
                .andExpect(jsonPath("$[1].employee_name", is(employee2.getName())))
                .andExpect(jsonPath("$[1].employee_age", is(employee2.getAge())))
                .andExpect(jsonPath("$[1].profile_image", is(employee2.getProfileImage())));

        verify(employeeService, times(1)).getAllEmployees();
    }

    @Test
    void testGetEmployeeById() throws Exception {
        when(employeeService.getEmployeeById("1")).thenReturn(Optional.of(employee1));

        mockMvc.perform(get("/employees/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(employee1.getId())))
                .andExpect(jsonPath("$.employee_name", is(employee1.getName())))
                .andExpect(jsonPath("$.employee_age", is(employee1.getAge())))
                .andExpect(jsonPath("$.profile_image", is(employee1.getProfileImage())));

        verify(employeeService, times(1)).getEmployeeById("1");
    }

    @Test
    void testGetEmployeeById_NotFound() throws Exception {
        when(employeeService.getEmployeeById("1")).thenReturn(Optional.empty());

        mockMvc.perform(get("/employees/1"))
                .andExpect(status().isNotFound());

        verify(employeeService, times(1)).getEmployeeById("1");
    }

    @Test
    void testCreateEmployee() throws Exception {
        Employee newEmployee = new Employee(null, "John Smith", 50020, 40, "image3.jpg");
        Employee createdEmployee = new Employee(3, "John Smith", 50020, 40, "image3.jpg");

        when(employeeService.createEmployee(any())).thenReturn(createdEmployee);

        mockMvc.perform(post("/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newEmployee)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(createdEmployee.getId())))
                .andExpect(jsonPath("$.employee_name", is(createdEmployee.getName())))
                .andExpect(jsonPath("$.employee_age", is(createdEmployee.getAge())))
                .andExpect(jsonPath("$.employee_salary", is(createdEmployee.getSalary())))
                .andExpect(jsonPath("$.profile_image", is(createdEmployee.getProfileImage())));

        verify(employeeService, times(1)).createEmployee(any());
    }

    void testDeleteEmployeeById() throws Exception {
        when(employeeService.deleteEmployeeById(anyString())).thenReturn("Tiger Nixon");
        mockMvc.perform(delete("/employees/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Tiger Nixon"));
        verify(employeeService, times(1)).deleteEmployeeById("1");
    }

    @Test
    void testGetHighestSalaryOfEmployees() throws Exception {
        when(employeeService.getHighestSalary()).thenReturn(50000);

        mockMvc.perform(get("/employees/highestSalary"))
                .andExpect(status().isOk())
                .andExpect(content().string("50000"));

        verify(employeeService, times(1)).getHighestSalary();
    }

    @Test
    void testGetTopTenHighestEarningEmployeeNames() throws Exception {
        List<String> topEarners = Arrays.asList("Will Smith", "Bill Gates");

        when(employeeService.getTopTenHighestEarningEmployeeNames()).thenReturn(topEarners);

        mockMvc.perform(get("/employees/topTenHighestEarningEmployeeNames"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0]", is("Will Smith")))
                .andExpect(jsonPath("$[1]", is("Bill Gates")));

        verify(employeeService, times(1)).getTopTenHighestEarningEmployeeNames();
    }
}


