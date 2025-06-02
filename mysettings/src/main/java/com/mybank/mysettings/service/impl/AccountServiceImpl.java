package com.mybank.mysettings.service.impl;

import com.mybank.common.entity.Account;
import com.mybank.mysettings.dto.AccountDto;
import com.mybank.mysettings.exception.ResourceNotFoundException;
import com.mybank.mysettings.mapper.AccountMapper;
import com.mybank.mysettings.repository.AccountRepository;
import com.mybank.mysettings.service.AccountService;
import com.mybank.mysettings.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;
    private final UserService userService;

    @Autowired
    public AccountServiceImpl(
            AccountRepository accountRepository,
            AccountMapper accountMapper,
            UserService userService) {
        this.accountRepository = accountRepository;
        this.accountMapper = accountMapper;
        this.userService = userService;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AccountDto> getAccountById(Long accountId) {
        return accountRepository.findById(accountId)
                .map(accountMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AccountDto> getAccountByAccountNumber(String accountNumber) {
        return accountRepository.findByAccountNumber(accountNumber)
                .map(accountMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AccountDto> getAccountsByCustomerId(Long customerId) {
        List<Account> accounts = accountRepository.findByCustomerCustomerId(customerId);
        return accountMapper.toDtoList(accounts);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AccountDto> getAccountsByUserId(Long userId) {
        // Get customer ID from user ID
        Long customerId = userService.getCustomerIdByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("No customer found for user ID: " + userId));
        
        // Get accounts by customer ID
        return getAccountsByCustomerId(customerId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AccountDto> getAccountsByCustomerIdAndAccountType(Long customerId, String accountTypeName) {
        List<Account> accounts = accountRepository.findByCustomerIdAndAccountTypeName(customerId, accountTypeName);
        return accountMapper.toDtoList(accounts);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AccountDto> getAccountsByUserIdAndAccountType(Long userId, String accountTypeName) {
        // Get customer ID from user ID
        Long customerId = userService.getCustomerIdByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("No customer found for user ID: " + userId));
        
        // Get accounts by customer ID and account type
        return getAccountsByCustomerIdAndAccountType(customerId, accountTypeName);
    }
}