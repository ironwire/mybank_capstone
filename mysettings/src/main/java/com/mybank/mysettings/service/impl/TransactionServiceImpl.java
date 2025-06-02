package com.mybank.mysettings.service.impl;

import com.mybank.common.entity.Account;
import com.mybank.common.entity.Transaction;
import com.mybank.mysettings.dto.TransactionDto;
import com.mybank.mysettings.exception.ResourceNotFoundException;
import com.mybank.mysettings.mapper.TransactionMapper;
import com.mybank.mysettings.repository.AccountRepository;
import com.mybank.mysettings.repository.TransactionRepository;
import com.mybank.mysettings.service.TransactionService;
import com.mybank.mysettings.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    private final TransactionMapper transactionMapper;
    private final UserService userService;

    @Autowired
    public TransactionServiceImpl(
            TransactionRepository transactionRepository,
            AccountRepository accountRepository,
            TransactionMapper transactionMapper,
            UserService userService) {
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
        this.transactionMapper = transactionMapper;
        this.userService = userService;
    }

    @Override
    @Transactional
    public TransactionDto createTransaction(TransactionDto transactionDto) {
        // Validate required fields
        if (transactionDto.getAccountId() == null) {
            throw new IllegalArgumentException("Account ID must not be null");
        }
        
        if (transactionDto.getAmount() == null) {
            throw new IllegalArgumentException("Transaction amount must not be null");
        }
        
        if (transactionDto.getTransactionType() == null || transactionDto.getTransactionType().trim().isEmpty()) {
            throw new IllegalArgumentException("Transaction type must not be null or empty");
        }
        
        // Find the account
        Account account = accountRepository.findById(transactionDto.getAccountId())
                .orElseThrow(() -> new ResourceNotFoundException("Account not found with ID: " + transactionDto.getAccountId()));
        
        // Create transaction entity
        Transaction transaction = new Transaction();
        transaction.setAccount(account);
        transaction.setAmount(transactionDto.getAmount());
        transaction.setTransactionType(transactionDto.getTransactionType());
        transaction.setDescription(transactionDto.getDescription());
        transaction.setReferenceNumber(transactionDto.getReferenceNumber());
        transaction.setReceivingAccountId(transactionDto.getReceivingAccountId());
        
        // Set transaction date if not provided
        if (transactionDto.getTransactionDate() != null) {
            transaction.setTransactionDate(transactionDto.getTransactionDate());
        } else {
            transaction.setTransactionDate(LocalDateTime.now());
        }
        
        // Set default status if not provided
        if (transactionDto.getTransactionStatus() != null) {
            transaction.setTransactionStatus(transactionDto.getTransactionStatus());
        } else {
            transaction.setTransactionStatus("Completed");
        }
        
        // Update account balance based on transaction type
        updateAccountBalance(account, transaction);
        
        // Save the transaction
        Transaction savedTransaction = transactionRepository.save(transaction);
        
        // Save the updated account
        accountRepository.save(account);
        
        // Convert back to DTO and return
        TransactionDto savedTransactionDto = transactionMapper.toDto(savedTransaction);
        savedTransactionDto.setBalanceAfterTransaction(account.getBalance());
        
        return savedTransactionDto;
    }
    
    @Override
    @Transactional
    public List<TransactionDto> createTransactions(List<TransactionDto> transactionDtos) {
        // Process each transaction and collect results
        return transactionDtos.stream()
                .map(this::createTransaction)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<TransactionDto> getTransactionById(Long transactionId) {
        return transactionRepository.findById(transactionId)
                .map(transactionMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TransactionDto> getTransactionsByAccountId(Long accountId) {
        List<Transaction> transactions = transactionRepository.findByAccountAccountId(accountId);
        return transactionMapper.toDtoList(transactions);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TransactionDto> getTransactionsByAccountNumber(String accountNumber, Pageable pageable) {
        Page<Transaction> transactionsPage = transactionRepository.findByAccountAccountNumber(accountNumber, pageable);
        return transactionsPage.map(transactionMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TransactionDto> getTransactionsByAccountAndDateRange(
            Long accountId, LocalDateTime startDate, LocalDateTime endDate) {
        List<Transaction> transactions = transactionRepository.findByAccountAndDateRange(accountId, startDate, endDate);
        return transactionMapper.toDtoList(transactions);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TransactionDto> getTransactionsByCustomerId(Long customerId) {
        List<Transaction> transactions = transactionRepository.findByCustomerId(customerId);
        return transactionMapper.toDtoList(transactions);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TransactionDto> getTransactionsByType(String transactionType) {
        List<Transaction> transactions = transactionRepository.findByTransactionType(transactionType);
        return transactionMapper.toDtoList(transactions);
    }
    
    /**
     * Update account balance based on transaction type
     * 
     * @param account the account to update
     * @param transaction the transaction
     */
    private void updateAccountBalance(Account account, Transaction transaction) {
        BigDecimal amount = transaction.getAmount();
        String transactionType = transaction.getTransactionType();
        
        if (transactionType == null) {
            throw new IllegalArgumentException("Transaction type cannot be null");
        }
        
        // Normalize transaction type to uppercase for case-insensitive comparison
        String normalizedType = transactionType.toUpperCase();
        
        // Determine if this is a credit (deposit) or debit (withdrawal) transaction
        boolean isCredit = isCreditTransaction(normalizedType);
        
        if (isCredit) {
            // Credit transaction - add to balance
            account.setBalance(account.getBalance().add(amount));
        } else {
            // Debit transaction - subtract from balance
            // Check if sufficient funds
            if (account.getBalance().compareTo(amount) < 0) {
                throw new IllegalStateException("Insufficient funds for transaction");
            }
            account.setBalance(account.getBalance().subtract(amount));
        }
    }

    /**
     * Determine if a transaction type is a credit (deposit) transaction
     * 
     * @param transactionType the transaction type (normalized to uppercase)
     * @return true if it's a credit transaction, false if it's a debit transaction
     */
    private boolean isCreditTransaction(String transactionType) {
        // Credit transaction keywords
        String[] creditKeywords = {
            "DEPOSIT", "CREDIT", "TRANSFER_IN", "SALARY", "入账", "存款", "转入", "工资"
        };
        
        // Debit transaction keywords
        String[] debitKeywords = {
            "WITHDRAWAL", "DEBIT", "TRANSFER_OUT", "取款", "转出", "支出", "扣款"
        };
        
        // Check if the transaction type contains any credit keywords
        for (String keyword : creditKeywords) {
            if (transactionType.contains(keyword)) {
                return true;
            }
        }
        
        // Check if the transaction type contains any debit keywords
        for (String keyword : debitKeywords) {
            if (transactionType.contains(keyword)) {
                return true;
            }
        }
        
        // If no keywords match, default to credit if the transaction type doesn't 
        // clearly indicate a withdrawal
        return !transactionType.contains("WITHDRAW") && !transactionType.contains("DEBIT");
    }

    @Override
    @Transactional(readOnly = true)
    public List<TransactionDto> getTransactionsByUserId(Long userId) {
        // Get customer ID from user ID
        Long customerId = userService.getCustomerIdByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("No customer found for user ID: " + userId));
        
        // Get transactions by customer ID
        return getTransactionsByCustomerId(customerId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TransactionDto> getTransactionsByUserIdAndDateRange(Long userId, LocalDateTime startDate, LocalDateTime endDate) {
        // Get customer ID from user ID
        Long customerId = userService.getCustomerIdByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("No customer found for user ID: " + userId));
        
        // Get accounts for this customer
        List<Account> accounts = accountRepository.findByCustomerCustomerId(customerId);
        
        if (accounts.isEmpty()) {
            return new ArrayList<>();
        }
        
        // Collect transactions from all accounts within the date range
        List<Transaction> allTransactions = new ArrayList<>();
        
        for (Account account : accounts) {
            List<Transaction> accountTransactions = 
                transactionRepository.findByAccountAndDateRange(account.getAccountId(), startDate, endDate);
            allTransactions.addAll(accountTransactions);
        }
        
        return transactionMapper.toDtoList(allTransactions);
    }
}
