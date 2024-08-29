package com.example.rqchallenge.employee.service;

import com.example.rqchallenge.employees.model.ApiResponse;
import com.example.rqchallenge.employees.model.Employee;
import com.example.rqchallenge.employees.service.impl.EmployeeServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class EmployeeServiceImplTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    private Employee employee1;
    private Employee employee2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        employee1 = new Employee();
        employee1.setId(1);
        employee1.setName("Tiger Nixon");
        employee1.setSalary(320800);
        employee1.setAge(61);
        employee1.setProfileImage("");

        employee2 = new Employee();
        employee2.setId(2);
        employee2.setName("Jane Smith");
        employee2.setSalary(500000);
        employee2.setAge(30);
        employee2.setProfileImage("");
    }

    @Test
    void testGetAllEmployees() {
        ApiResponse<List<Employee>> apiResponse = new ApiResponse<>();
        apiResponse.setStatus("success");
        apiResponse.setData(Arrays.asList(employee1, employee2));
        apiResponse.setMessage("Successfully! Record has been fetched.");

        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), eq(null), any(ParameterizedTypeReference.class)))
                .thenReturn(new ResponseEntity<>(apiResponse, HttpStatus.OK));

        List<Employee> employees = employeeService.getAllEmployees();

        assertNotNull(employees);
        assertEquals(2, employees.size());
        assertEquals("Tiger Nixon", employees.get(0).getName());
        verify(restTemplate, times(1)).exchange(anyString(), eq(HttpMethod.GET), eq(null), any(ParameterizedTypeReference.class));
    }

    @Test
    void testSearchEmployeesByName() {
        ApiResponse<List<Employee>> apiResponse = new ApiResponse<>();
        apiResponse.setStatus("success");
        apiResponse.setData(Arrays.asList(employee1, employee2));
        apiResponse.setMessage("Successfully! Record has been fetched.");

        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), eq(null), any(ParameterizedTypeReference.class)))
                .thenReturn(new ResponseEntity<>(apiResponse, HttpStatus.OK));

        List<Employee> result = employeeService.searchEmployeesByName("Jane");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Jane Smith", result.get(0).getName());
    }

    @Test
    void testGetEmployeeById() {
        ApiResponse<Employee> apiResponse = new ApiResponse<>();
        apiResponse.setStatus("success");
        apiResponse.setData(employee1);
        apiResponse.setMessage("Successfully! Record has been fetched.");

        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), eq(null), any(ParameterizedTypeReference.class)))
                .thenReturn(new ResponseEntity<>(apiResponse, HttpStatus.OK));

        Optional<Employee> result = employeeService.getEmployeeById("1");

        assertTrue(result.isPresent());
        assertEquals("Tiger Nixon", result.get().getName());
    }

    @Test
    void testGetEmployeeById_NotFound() {
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), eq(null), any(ParameterizedTypeReference.class)))
                .thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND));

        Optional<Employee> result = employeeService.getEmployeeById("1");

        assertFalse(result.isPresent());
    }

    @Test
    void testGetHighestSalary() {
        ApiResponse<List<Employee>> apiResponse = new ApiResponse<>();
        apiResponse.setStatus("success");
        apiResponse.setData(Arrays.asList(employee1, employee2));
        apiResponse.setMessage("Successfully! Record has been fetched.");

        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), eq(null), any(ParameterizedTypeReference.class)))
                .thenReturn(new ResponseEntity<>(apiResponse, HttpStatus.OK));

        int highestSalary = employeeService.getHighestSalary();

        assertEquals(500000, highestSalary);
    }

    @Test
    void testGetTopTenHighestEarningEmployeeNames() {
        ApiResponse<List<Employee>> apiResponse = new ApiResponse<>();
        apiResponse.setStatus("success");
        apiResponse.setData(Arrays.asList(employee1, employee2));
        apiResponse.setMessage("Successfully! Record has been fetched.");

        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), eq(null), any(ParameterizedTypeReference.class)))
                .thenReturn(new ResponseEntity<>(apiResponse, HttpStatus.OK));

        List<String> topEarners = employeeService.getTopTenHighestEarningEmployeeNames();

        assertNotNull(topEarners);
        assertEquals(2, topEarners.size());
        assertEquals("Jane Smith", topEarners.get(0));
    }

    @Test
    void testCreateEmployee() {
        ApiResponse<Employee> apiResponse = new ApiResponse<>();
        apiResponse.setStatus("success");
        apiResponse.setData(employee1);
        apiResponse.setMessage("Successfully! Record has been created.");

        when(restTemplate.exchange(anyString(), eq(HttpMethod.POST), any(), any(ParameterizedTypeReference.class)))
                .thenReturn(new ResponseEntity<>(apiResponse, HttpStatus.CREATED));

        Map<String, Object> employeeInput = new HashMap<>();
        employeeInput.put("name", "Tiger Nixon");
        employeeInput.put("salary", "320800");
        employeeInput.put("age", "61");
        employeeInput.put("profile_image", "");

        Employee createdEmployee = employeeService.createEmployee(employeeInput);

        assertNotNull(createdEmployee);
        assertEquals("Tiger Nixon", createdEmployee.getName());
        verify(restTemplate, times(1)).exchange(anyString(), eq(HttpMethod.POST), any(), any(ParameterizedTypeReference.class));
    }

    @Test
    void testDeleteEmployeeById() {
        doNothing().when(restTemplate).delete(anyString());

        employeeService.deleteEmployeeById("1");

        verify(restTemplate, times(1)).delete(anyString());
    }
}
