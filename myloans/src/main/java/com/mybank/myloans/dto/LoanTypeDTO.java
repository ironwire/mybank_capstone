package com.mybank.myloans.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoanTypeDTO {
    private Long loanTypeId;
    private String name;
    private String description;
    private BigDecimal minInterestRate;
    private BigDecimal maxInterestRate;
    private Integer minTerm;
    private Integer maxTerm;
    private BigDecimal minimumAmount;
    private BigDecimal maximumAmount;
    private Boolean isActive;
}