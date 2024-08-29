package com.example.rqchallenge.employees.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Employee {
    @JsonProperty("id")
    Integer id;
    @JsonProperty("employee_name")
    String name;
    @JsonProperty("employee_salary")
    Integer salary;
    @JsonProperty("employee_age")
    Integer age;
    @JsonProperty("profile_image")
    String profileImage;

}
