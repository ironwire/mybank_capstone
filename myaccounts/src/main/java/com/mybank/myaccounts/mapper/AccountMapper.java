package com.mybank.myaccounts.mapper;

import com.mybank.common.entity.Account;
import com.mybank.myaccounts.dto.AccountDto;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper for converting between Account entity and AccountDto
 */
@Component
public class AccountMapper {
    
    /**
     * Convert Account entity to AccountDto
     */
    public AccountDto toDto(Account account) {
        if (account == null) {
            return null;
        }
        
        AccountDto.AccountDtoBuilder builder = AccountDto.builder()
                .accountId(account.getAccountId())
                .accountNumber(account.getAccountNumber())
                .balance(account.getBalance())
                .openingDate(account.getOpeningDate())
                .status(account.getStatus())
                .notes(account.getNotes())
                .createdAt(account.getCreatedAt())
                .updatedAt(account.getUpdatedAt());
        
        // Set customer information if available
        if (account.getCustomer() != null) {
            builder.customerId(account.getCustomer().getCustomerId())
                   .customerFirstName(account.getCustomer().getFirstName())
                   .customerLastName(account.getCustomer().getLastName())
                   .customerEmail(account.getCustomer().getEmail());
        }
        
        // Set account type information if available
        if (account.getAccountType() != null) {
            builder.accountTypeId(account.getAccountType().getAccountTypeId())
                   .accountTypeName(account.getAccountType().getName())
                   .interestRate(account.getAccountType().getInterestRate());
        }
        
        // Set transaction summary if available
        if (account.getTransactions() != null && !account.getTransactions().isEmpty()) {
            builder.transactionCount(account.getTransactions().size())
                   .lastTransactionDate(account.getTransactions().stream()
                           .map(t -> t.getTransactionDate())
                           .max(LocalDateTime::compareTo)
                           .orElse(null));
        }
        
        return builder.build();
    }
    
    /**
     * Convert a list of Account entities to a list of AccountDtos
     */
    public List<AccountDto> toDtoList(List<Account> accounts) {
        if (accounts == null) {
            return null;
        }
        
        return accounts.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
}