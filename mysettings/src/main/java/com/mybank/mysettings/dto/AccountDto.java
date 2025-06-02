package com.mybank.mysettings.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountDto {
    private Long accountId;
    private Long customerId;
    private String customerName;
    private Long accountTypeId;
    private String accountTypeName;
    private String accountNumber;
    private BigDecimal balance;
    private LocalDate openingDate;
    private String status;
    private String notes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<TransactionDto> recentTransactions;
    
    // Additional fields for account summary
    private BigDecimal totalDeposits;
    private BigDecimal totalWithdrawals;
    private Integer transactionCount;
    private LocalDateTime lastTransactionDate;
}