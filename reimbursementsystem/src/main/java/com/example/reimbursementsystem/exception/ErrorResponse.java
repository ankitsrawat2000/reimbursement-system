package com.example.reimbursementsystem.exception;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class ErrorResponse {
    private int status;
    private String message;
    private Map<String, String> errors = new HashMap<>();

    public ErrorResponse(int status, String message) {
        this.status = status;
        this.message = message;
    }
}