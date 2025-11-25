package com.example.reimbursementsystem.service;

import com.example.reimbursementsystem.dto.ClaimReportResponse;
import com.example.reimbursementsystem.dto.CreateExpenseClaimRequest;
import com.example.reimbursementsystem.dto.ExpenseClaimResponse;
import com.example.reimbursementsystem.dto.UpdateClaimStatusRequest;
import com.example.reimbursementsystem.entity.ClaimStatus;
import com.example.reimbursementsystem.entity.Employee;
import com.example.reimbursementsystem.entity.ExpenseClaim;
import com.example.reimbursementsystem.exception.EmployeeNotFoundException;
import com.example.reimbursementsystem.repository.EmployeeRepository;
import com.example.reimbursementsystem.repository.ExpenseClaimRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ExpenseClaimService {

    private final ExpenseClaimRepository claimRepo;
    private final EmployeeRepository employeeRepo;

    public ExpenseClaimService(ExpenseClaimRepository claimRepo, EmployeeRepository employeeRepo) {
        this.claimRepo = claimRepo;
        this.employeeRepo = employeeRepo;
    }

    public ExpenseClaimResponse createClaim(Long employeeId, CreateExpenseClaimRequest req) {
        Employee emp = employeeRepo.findById(employeeId)
                .orElseThrow(() -> new EmployeeNotFoundException(employeeId));

        ExpenseClaim claim = new ExpenseClaim();
        claim.setEmployee(emp);
        claim.setDescription(req.getDescription());
        claim.setAmount(req.getAmount());
        claim.setStatus(ClaimStatus.Pending);
        claim.setDateSubmitted(LocalDate.now());

        ExpenseClaim saved = claimRepo.save(claim);
        return mapToResponse(saved);
    }

    private ExpenseClaimResponse mapToResponse(ExpenseClaim saved) {
        ExpenseClaimResponse r = new ExpenseClaimResponse();
        r.setId(saved.getId());
        r.setEmployeeId(saved.getEmployee().getId());
        r.setDateSubmitted(saved.getDateSubmitted());
        r.setDescription(saved.getDescription());
        r.setAmount(saved.getAmount());
        r.setStatus(saved.getStatus().name());
        return r;
    }

    public List<ExpenseClaimResponse> getClaimsForEmployee(Long employeeId) {

        Employee emp = employeeRepo.findById(employeeId)
                .orElseThrow(() -> new EmployeeNotFoundException(employeeId));

        List<ExpenseClaim> claims = claimRepo.findByEmployee(emp);

        return claims.stream()
                .map(this::mapToResponse)
                .toList();
    }

    public ExpenseClaimResponse updateClaimStatus(Long claimId, UpdateClaimStatusRequest req) {

        ExpenseClaim claim = claimRepo.findById(claimId)
                .orElseThrow(() -> new RuntimeException("Claim not found: " + claimId));

        ClaimStatus newStatus;
        try {
            newStatus = ClaimStatus.valueOf(req.getStatus());
        } catch (IllegalArgumentException ex) {
            throw new RuntimeException("Invalid status. Allowed: Approved, Rejected");
        }

        if (newStatus == ClaimStatus.Pending) {
            throw new RuntimeException("You cannot set status to Pending.");
        }

        claim.setStatus(newStatus);

        ExpenseClaim saved = claimRepo.save(claim);
        return mapToResponse(saved);
    }

    public List<ClaimReportResponse> getClaimsReport(LocalDate start, LocalDate end) {

        List<ExpenseClaim> claims = claimRepo
                .findByDateSubmittedBetween(start, end);

        return claims.stream().map(c -> {
            ClaimReportResponse r = new ClaimReportResponse();
            r.setEmployeeName(c.getEmployee().getName());
            r.setDepartment(c.getEmployee().getDepartment());
            r.setDescription(c.getDescription());
            r.setAmount(c.getAmount());
            r.setStatus(c.getStatus().name());
            r.setDateSubmitted(c.getDateSubmitted());
            return r;
        }).toList();
    }



}
