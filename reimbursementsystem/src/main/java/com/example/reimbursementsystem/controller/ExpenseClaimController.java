package com.example.reimbursementsystem.controller;

import com.example.reimbursementsystem.dto.ClaimReportResponse;
import com.example.reimbursementsystem.dto.CreateExpenseClaimRequest;
import com.example.reimbursementsystem.dto.ExpenseClaimResponse;
import com.example.reimbursementsystem.dto.UpdateClaimStatusRequest;
import com.example.reimbursementsystem.service.ExpenseClaimService;
import com.example.reimbursementsystem.service.ReportService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
public class ExpenseClaimController {

    private final ExpenseClaimService claimService;

    private final ReportService reportService;


    public ExpenseClaimController(ExpenseClaimService claimService, ReportService reportService) {
        this.claimService = claimService;
        this.reportService = reportService;
    }

    @PostMapping("/employees/{id}/claims")
    public ResponseEntity<?> createClaim(
            @PathVariable("id") Long employeeId,
            @Valid @RequestBody CreateExpenseClaimRequest request) {

        ExpenseClaimResponse response = claimService.createClaim(employeeId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/employees/{id}/claims")
    public ResponseEntity<?> getClaimsForEmployee(
            @PathVariable("id") Long employeeId) {
        List<ExpenseClaimResponse> claims = claimService.getClaimsForEmployee(employeeId);
        return ResponseEntity.ok(claims);
    }

    @PutMapping("/claims/{claimId}/status")
    public ResponseEntity<?> updateClaimStatus(
            @PathVariable Long claimId,
            @Valid @RequestBody UpdateClaimStatusRequest request) {

        ExpenseClaimResponse response = claimService.updateClaimStatus(claimId, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/reports/claims")
    public ResponseEntity<?> getClaimsReport(
            @RequestParam("start_date") String startDateStr,
            @RequestParam("end_date") String endDateStr,
            @RequestParam(value = "format", defaultValue = "json") String format) {

        if (!format.equalsIgnoreCase("json") && !format.equalsIgnoreCase("pdf")) {
            return ResponseEntity.badRequest().body("Invalid format. Use 'json' or 'pdf'.");
        }

        LocalDate startDate;
        LocalDate endDate;

        try {
            startDate = LocalDate.parse(startDateStr);
            endDate = LocalDate.parse(endDateStr);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Malformed date. Use YYYY-MM-DD.");
        }

        if (endDate.isBefore(startDate)) {
            return ResponseEntity.badRequest().body("end_date cannot be before start_date");
        }

        if (format.equalsIgnoreCase("json")) {
            List<ClaimReportResponse> data = claimService.getClaimsReport(startDate, endDate);
            return ResponseEntity.ok(data);
        }

        byte[] pdfBytes = reportService.generateReportPdf(startDate, endDate);

        return ResponseEntity.ok()
                .header("Content-Type", "application/pdf")
                .header("Content-Disposition", "attachment; filename=claims_report.pdf")
                .body(pdfBytes);
    }


}