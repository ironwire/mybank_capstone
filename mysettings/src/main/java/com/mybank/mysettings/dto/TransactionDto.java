package com.mybank.mysettings.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionDto {
    private Long transactionId;
    private Long accountId;
    private String accountNumber;
    private BigDecimal amount;
    private String transactionType;
    private LocalDateTime transactionDate;
    private String description;
    private String referenceNumber;
    private Integer receivingAccountId;
    private String transactionStatus;
    private LocalDateTime createdAt;
    
    // Additional fields for transaction details
    private String customerName;
    private String accountTypeName;
    private BigDecimal balanceAfterTransaction;
}