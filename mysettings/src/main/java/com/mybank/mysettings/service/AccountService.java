package com.mybank.mysettings.service;

import com.mybank.mysettings.dto.AccountDto;

import java.util.List;
import java.util.Optional;

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
     * Get accounts by user ID and account type
     * First retrieves the customer ID associated with the user ID,
     * then finds all accounts for that customer with the specified account type
     * 
     * @param userId the user ID
     * @param accountTypeName the account type name
     * @return list of accounts
     */
    List<AccountDto> getAccountsByUserIdAndAccountType(Long userId, String accountTypeName);
}