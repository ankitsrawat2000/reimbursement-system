package com.example.reimbursementsystem.exception;

public class ClaimNotFoundException extends RuntimeException {
    public ClaimNotFoundException(Long id) {
        super("Claim not found with ID: " + id);
    }
}
