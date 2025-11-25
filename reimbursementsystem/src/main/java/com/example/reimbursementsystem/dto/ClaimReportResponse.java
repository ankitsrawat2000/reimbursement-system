package com.example.reimbursementsystem.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class ClaimReportResponse {
    private String employeeName;
    private String department;
    private String description;
    private BigDecimal amount;
    private String status;
    private LocalDate dateSubmitted;
}
