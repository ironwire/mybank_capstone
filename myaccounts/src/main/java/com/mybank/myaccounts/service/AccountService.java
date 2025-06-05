package com.mybank.myaccounts.service;

import com.mybank.myaccounts.dto.AccountDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Service interface for account operations
 */
public interface AccountService {
    
    /**
     * Get an account by ID
     * 
     * @param accountId the account ID
     * @return the account if found
     */
    Optional<AccountDto> getAccountById(Long accountId);
    
    /**
     * Get an account by account number
     * 
     * @param accountNumber the account number
     * @return the account if found
     */
    Optional<AccountDto> getAccountByAccountNumber(String accountNumber);
    
    /**
     * Get all accounts for a customer
     * 
     * @param customerId the customer ID
     * @return list of accounts
     */
    List<AccountDto> getAccountsByCustomerId(Long customerId);
    
    /**
     * Get all accounts for a user
     * First retrieves the customer ID associated with the user ID,
     * then finds all accounts for that customer
     * 
     * @param userId the user ID
     * @return list of accounts
     */
    List<AccountDto> getAccountsByUserId(Long userId);
    
    /**
     * Get accounts by customer ID and account type
     * 
     * @param customerId the customer ID
     * @param accountTypeName the account type name
     * @return list of accounts
     */
    List<AccountDto> getAccountsByCustomerIdAndAccountType(Long customerId, String accountTypeName);
    
    /**
     * Get accounts with balance greater than specified amount
     * 
     * @param minimumBalance the minimum balance
     * @return list of accounts
     */
    List<AccountDto> getAccountsWithBalanceGreaterThan(BigDecimal minimumBalance);
    
    /**
     * Get all accounts with pagination
     * 
     * @param pageable pagination information
     * @return page of accounts
     */
    Page<AccountDto> getAllAccounts(Pageable pageable);
    
    /**
     * Create a new account
     * 
     * @param accountDto the account data
     * @return the created account
     */
    AccountDto createAccount(AccountDto accountDto);
    
    /**
     * Update an existing account
     * 
     * @param accountId the account ID
     * @param accountDto the updated account data
     * @return the updated account
     */
    AccountDto updateAccount(Long accountId, AccountDto accountDto);
    
    /**
     * Update account status
     * 
     * @param accountId the account ID
     * @param status the new status
     * @return the updated account
     */
    AccountDto updateAccountStatus(Long accountId, String status);
    
    /**
     * Close an account
     * 
     * @param accountId the account ID
     * @return true if successful
     */
    boolean closeAccount(Long accountId);
    
    /**
     * Get account balance
     * 
     * @param accountId the account ID
     * @return the account balance
     */
    Optional<BigDecimal> getAccountBalance(Long accountId);
    
    /**
     * Update account balance
     * 
     * @param accountId the account ID
     * @param amount the amount to add (positive) or subtract (negative)
     * @return the updated account
     */
    AccountDto updateAccountBalance(Long accountId, BigDecimal amount);
}