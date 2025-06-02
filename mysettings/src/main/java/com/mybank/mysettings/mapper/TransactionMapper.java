package com.mybank.mysettings.mapper;

import com.mybank.common.entity.Transaction;
import com.mybank.mysettings.dto.TransactionDto;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class TransactionMapper {

    /**
     * Convert Transaction entity to TransactionDto
     */
    public TransactionDto toDto(Transaction transaction) {
        if (transaction == null) {
            return null;
        }

        TransactionDto dto = new TransactionDto();
        dto.setTransactionId(transaction.getTransactionId());
        
        // Safely get account ID
        if (transaction.getAccount() != null) {
            dto.setAccountId(transaction.getAccount().getAccountId());
            if (transaction.getAccount().getAccountNumber() != null) {
                dto.setAccountNumber(transaction.getAccount().getAccountNumber());
            }
        }
        
        dto.setAmount(transaction.getAmount());
        dto.setTransactionType(transaction.getTransactionType());
        dto.setTransactionDate(transaction.getTransactionDate());
        dto.setDescription(transaction.getDescription());
        dto.setReferenceNumber(transaction.getReferenceNumber());
        dto.setReceivingAccountId(transaction.getReceivingAccountId());
        dto.setTransactionStatus(transaction.getTransactionStatus());
        dto.setCreatedAt(transaction.getCreatedAt());
        
        return dto;
    }

    /**
     * Convert TransactionDto to Transaction entity
     * Note: This does not set the account relationship - that must be done separately
     */
    public Transaction toEntity(TransactionDto dto) {
        if (dto == null) {
            return null;
        }

        Transaction transaction = new Transaction();
        
        // Only set ID if it's not a new transaction (ID is not null)
        if (dto.getTransactionId() != null) {
            transaction.setTransactionId(dto.getTransactionId());
        }
        
        transaction.setAmount(dto.getAmount());
        transaction.setTransactionType(dto.getTransactionType());
        transaction.setTransactionDate(dto.getTransactionDate());
        transaction.setDescription(dto.getDescription());
        transaction.setReferenceNumber(dto.getReferenceNumber());
        transaction.setReceivingAccountId(dto.getReceivingAccountId());
        transaction.setTransactionStatus(dto.getTransactionStatus());
        
        // Account must be set separately
        
        return transaction;
    }

    /**
     * Convert list of Transaction entities to list of TransactionDtos
     */
    public List<TransactionDto> toDtoList(List<Transaction> transactions) {
        return transactions.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
}
