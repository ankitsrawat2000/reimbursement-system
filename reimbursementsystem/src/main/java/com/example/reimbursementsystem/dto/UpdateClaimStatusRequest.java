package com.example.reimbursementsystem.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateClaimStatusRequest {

    @NotBlank(message = "Status is required")
    private String status;
}
