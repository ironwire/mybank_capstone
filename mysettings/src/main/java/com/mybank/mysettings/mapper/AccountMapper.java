package com.mybank.mysettings.mapper;

import com.mybank.common.entity.Account;
import com.mybank.mysettings.dto.AccountDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class AccountMapper {
    
    private final TransactionMapper transactionMapper;
    
    @Autowired
    public AccountMapper(TransactionMapper transactionMapper) {
        this.transactionMapper = transactionMapper;
    }
    
    public AccountDto toDto(Account account) {
        if (account == null) {
            return null;
        }
        
        AccountDto dto = new AccountDto();
        dto.setAccountId(account.getAccountId());
        
        if (account.getCustomer() != null) {
            dto.setCustomerId(account.getCustomer().getCustomerId());
            dto.setCustomerName(account.getCustomer().getFirstName() + " " + account.getCustomer().getLastName());
        }
        
        if (account.getAccountType() != null) {
            dto.setAccountTypeId(account.getAccountType().getAccountTypeId());
            dto.setAccountTypeName(account.getAccountType().getName());
        }
        
        dto.setAccountNumber(account.getAccountNumber());
        dto.setBalance(account.getBalance());
        dto.setOpeningDate(account.getOpeningDate());
        dto.setStatus(account.getStatus());
        dto.setNotes(account.getNotes());
        dto.setCreatedAt(account.getCreatedAt());
        dto.setUpdatedAt(account.getUpdatedAt());
        
        // Map recent transactions if available
        if (account.getTransactions() != null && !account.getTransactions().isEmpty()) {
            dto.setRecentTransactions(account.getTransactions().stream()
                    .limit(5) // Only include the 5 most recent transactions
                    .map(transactionMapper::toDto)
                    .collect(Collectors.toList()));
            
            // Set last transaction date
            dto.setLastTransactionDate(account.getTransactions().stream()
                    .map(t -> t.getTransactionDate())
                    .max(LocalDateTime::compareTo)
                    .orElse(null));
            
            // Calculate transaction statistics
            dto.setTransactionCount(account.getTransactions().size());
            
            // Calculate total deposits and withdrawals
            BigDecimal totalDeposits = account.getTransactions().stream()
                    .filter(t -> "DEPOSIT".equals(t.getTransactionType()) || "CREDIT".equals(t.getTransactionType()))
                    .map(t -> t.getAmount())
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            
            BigDecimal totalWithdrawals = account.getTransactions().stream()
                    .filter(t -> "WITHDRAWAL".equals(t.getTransactionType()) || "DEBIT".equals(t.getTransactionType()))
                    .map(t -> t.getAmount())
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            
            dto.setTotalDeposits(totalDeposits);
            dto.setTotalWithdrawals(totalWithdrawals);
        }
        
        return dto;
    }
    
    public List<AccountDto> toDtoList(List<Account> accounts) {
        if (accounts == null) {
            return null;
        }
        
        return accounts.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
    
    public Account toEntity(AccountDto dto) {
        if (dto == null) {
            return null;
        }
        
        Account account = new Account();
        
        // Don't set ID for new entities
        if (dto.getAccountId() != null) {
            account.setAccountId(dto.getAccountId());
        }
        
        account.setAccountNumber(dto.getAccountNumber());
        account.setBalance(dto.getBalance());
        account.setOpeningDate(dto.getOpeningDate());
        account.setStatus(dto.getStatus());
        account.setNotes(dto.getNotes());
        
        // Customer and AccountType must be set separately
        
        return account;
    }
    
    public void updateEntityFromDto(AccountDto dto, Account account) {
        if (dto == null || account == null) {
            return;
        }
        
        if (dto.getAccountNumber() != null) {
            account.setAccountNumber(dto.getAccountNumber());
        }
        
        if (dto.getBalance() != null) {
            account.setBalance(dto.getBalance());
        }
        
        if (dto.getOpeningDate() != null) {
            account.setOpeningDate(dto.getOpeningDate());
        }
        
        if (dto.getStatus() != null) {
            account.setStatus(dto.getStatus());
        }
        
        if (dto.getNotes() != null) {
            account.setNotes(dto.getNotes());
        }
        
        // Customer and AccountType must be updated separately
    }
}