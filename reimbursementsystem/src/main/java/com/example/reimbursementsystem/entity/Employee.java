package com.example.reimbursementsystem.entity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name="employee")
public class Employee {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name is required")
    @Column(name="name", length=100, nullable=false, unique=false)
    private String name;

    @Column(name="department", length=100, nullable=true, unique=false)
    private String department;

    @Email(message = "Invalid email format")
    @NotBlank(message = "Email is required")
    @Column(name="email", length=150, nullable=false, unique=true)
    private String email;

    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ExpenseClaim> expenseClaims;

}
