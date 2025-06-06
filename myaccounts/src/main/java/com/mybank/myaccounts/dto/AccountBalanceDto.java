package com.mybank.myaccounts.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * DTO for account balance information
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountBalanceDto {
    private Long accountId;
    private String accountName;
    private String accountNumber;
    private String accountType;
    private BigDecimal balance;
}