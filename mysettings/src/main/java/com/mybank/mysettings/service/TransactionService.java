package com.mybank.mysettings.service;

import com.mybank.mysettings.dto.TransactionDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface TransactionService {
    
    /**
     * Create a new transaction
     * 
     * @param transactionDto the transaction data
     * @return the created transaction
     */
    TransactionDto createTransaction(TransactionDto transactionDto);
    
    /**
     * Create multiple transactions in a batch
     * 
     * @param transactionDtos list of transaction DTOs to create
     * @return list of created transactions
     */
    List<TransactionDto> createTransactions(List<TransactionDto> transactionDtos);
    
    /**
     * Get a transaction by ID
     * 
     * @param transactionId the transaction ID
     * @return the transaction if found
     */
    Optional<TransactionDto> getTransactionById(Long transactionId);
    
    /**
     * Get all transactions for an account
     * 
     * @param accountId the account ID
     * @return list of transactions
     */
    List<TransactionDto> getTransactionsByAccountId(Long accountId);
    
    /**
     * Get transactions for an account with pagination
     * 
     * @param accountNumber the account number
     * @param pageable pagination information
     * @return page of transactions
     */
    Page<TransactionDto> getTransactionsByAccountNumber(String accountNumber, Pageable pageable);
    
    /**
     * Get transactions for an account within a date range
     * 
     * @param accountId the account ID
     * @param startDate the start date
     * @param endDate the end date
     * @return list of transactions
     */
    List<TransactionDto> getTransactionsByAccountAndDateRange(Long accountId, LocalDateTime startDate, LocalDateTime endDate);
    
    /**
     * Get all transactions for a customer
     * 
     * @param customerId the customer ID
     * @return list of transactions
     */
    List<TransactionDto> getTransactionsByCustomerId(Long customerId);
    
    /**
     * Get transactions by type
     * 
     * @param transactionType the transaction type
     * @return list of transactions
     */
    List<TransactionDto> getTransactionsByType(String transactionType);
    
    /**
     * Get all transactions for a user
     * First retrieves the customer ID associated with the user ID,
     * then finds all transactions for that customer
     * 
     * @param userId the user ID
     * @return list of transactions
     */
    List<TransactionDto> getTransactionsByUserId(Long userId);
    
    /**
     * Get transactions for a user within a date range
     * First retrieves the customer ID associated with the user ID,
     * then finds all transactions for that customer within the date range
     * 
     * @param userId the user ID
     * @param startDate the start date
     * @param endDate the end date
     * @return list of transactions
     */
    List<TransactionDto> getTransactionsByUserIdAndDateRange(Long userId, LocalDateTime startDate, LocalDateTime endDate);
}
