package com.example.reimbursementsystem.dto;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class ExpenseClaimResponse {
    private Long id;
    private Long employeeId;
    private LocalDate dateSubmitted;
    private String description;
    private BigDecimal amount;
    private String status;
}
