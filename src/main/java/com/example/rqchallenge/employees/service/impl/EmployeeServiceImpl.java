package com.example.rqchallenge.employees.service.impl;

import com.example.rqchallenge.employees.exception.RemoteApiException;
import com.example.rqchallenge.employees.model.ApiResponse;
import com.example.rqchallenge.employees.model.Employee;
import com.example.rqchallenge.employees.service.IEmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EmployeeServiceImpl implements IEmployeeService {

    private static final String baseUrl = "https://dummy.restapiexample.com/api/v1";
    private final RestTemplate restTemplate;

    @Autowired
    public EmployeeServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public List<Employee> getAllEmployees() {
        try {
            ResponseEntity<ApiResponse<List<Employee>>> response = restTemplate.exchange(
                    baseUrl + "/employees",
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<ApiResponse<List<Employee>>>() {
                    }
            );
            return response.getBody().getData();
        } catch (Exception e) {
            throw new RemoteApiException("Failed to retrieve employees from the remote API." + e.getMessage());
        }
    }

    @Override
    public List<Employee> searchEmployeesByName(String searchString) {
        try {
            List<Employee> allEmployees = getAllEmployees();
            return allEmployees.stream()
                    .filter(e -> e.getName().toLowerCase().contains(searchString.toLowerCase()))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RemoteApiException("Failed to search employees by name from the remote API: " + e.getMessage());
        }
    }

    @Override
    public Optional<Employee> getEmployeeById(String id) {
        try {
            ResponseEntity<ApiResponse<Employee>> response = restTemplate.exchange(
                    baseUrl + "/employee/" + id,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<ApiResponse<Employee>>() {
                    }
            );
            return Optional.ofNullable(response.getBody().getData());
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                return Optional.empty();
            }
            throw new RemoteApiException("Failed to retrieve employee by ID from the remote API: " + e.getMessage());
        }
    }

    @Override
    public int getHighestSalary() {
        try {
            List<Employee> allEmployees = getAllEmployees();
            return allEmployees.stream().mapToInt(Employee::getSalary).max().orElse(0);
        } catch (Exception e) {
            throw new RemoteApiException("Failed to retrieve the highest salary from the remote API: " + e.getMessage());
        }
    }

    @Override
    public List<String> getTopTenHighestEarningEmployeeNames() {
        try {
            List<Employee> allEmployees = getAllEmployees();
            return allEmployees.stream()
                    .sorted(Comparator.comparingInt(Employee::getSalary).reversed())
                    .limit(10)
                    .map(Employee::getName)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RemoteApiException("Failed to retrieve the top ten highest-earning employee names from the remote API: " + e.getMessage());
        }
    }

    @Override
    public Employee createEmployee(Map<String, Object> employeeInput) {
        try {
            ResponseEntity<ApiResponse<Employee>> response = restTemplate.exchange(
                    baseUrl + "/create",
                    HttpMethod.POST,
                    new HttpEntity<>(employeeInput),
                    new ParameterizedTypeReference<ApiResponse<Employee>>() {
                    }
            );
            return response.getBody().getData();
        } catch (HttpClientErrorException e) {
            throw new RemoteApiException("Failed to create employee on the remote API: " + e.getMessage());
        }
    }

    @Override
    public void deleteEmployeeById(String id) {
        try {
            restTemplate.delete(baseUrl + "/delete/" + id);
        } catch (HttpClientErrorException e) {
            throw new RemoteApiException("Failed to delete employee on the remote API: " + e.getMessage());
        }
    }
}

