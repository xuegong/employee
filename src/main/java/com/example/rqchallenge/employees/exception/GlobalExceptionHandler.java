package com.example.rqchallenge.employees.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RemoteApiException.class)
    public ResponseEntity<Map<String, String>> handleRemoteApiException(RemoteApiException ex) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", "Remote API Error");
        errorResponse.put("message", ex.getMessage());

        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}