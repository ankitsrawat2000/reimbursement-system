package com.example.reimbursementsystem.repository;

import com.example.reimbursementsystem.entity.Employee;
import com.example.reimbursementsystem.entity.ExpenseClaim;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface ExpenseClaimRepository extends JpaRepository<ExpenseClaim, Long> {

    List<ExpenseClaim> findByEmployee(Employee employee);

    List<ExpenseClaim> findByDateSubmittedBetween(LocalDate start, LocalDate end);

}