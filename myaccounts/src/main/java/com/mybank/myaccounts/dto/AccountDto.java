package com.mybank.myaccounts.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Data Transfer Object for Account information
 * Avoids circular references between Account and Customer entities
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountDto {
    private Long accountId;
    
    // Customer information
    private Long customerId;
    private String customerFirstName;
    private String customerLastName;
    private String customerEmail;
    
    // Account type information
    private Long accountTypeId;
    private String accountTypeName;
    private BigDecimal interestRate;
    
    // Account details
    private String accountNumber;
    private BigDecimal balance;
    private LocalDate openingDate;
    private String status;
    private String notes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // Transaction summary (optional)
    private Integer transactionCount;
    private LocalDateTime lastTransactionDate;
}