package com.example.rqchallenge.employees.service;

import com.example.rqchallenge.employees.model.Employee;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface IEmployeeService {
    List<Employee> getAllEmployees() throws IOException;

    List<Employee> searchEmployeesByName(String searchString);

    Optional<Employee> getEmployeeById(String id);

    int getHighestSalary();

    List<String> getTopTenHighestEarningEmployeeNames();

    Employee createEmployee(Map<String, Object> employeeInput);

    String deleteEmployeeById(String id);
}