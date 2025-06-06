package com.mybank.myaccounts.service.impl;

import com.mybank.common.entity.Account;
import com.mybank.common.entity.AccountType;
import com.mybank.common.entity.Customer;
import com.mybank.myaccounts.dto.AccountDto;
import com.mybank.myaccounts.dto.AccountBalanceDto;
import com.mybank.myaccounts.exception.InsufficientFundsException;
import com.mybank.myaccounts.exception.ResourceNotFoundException;
import com.mybank.myaccounts.mapper.AccountMapper;
import com.mybank.myaccounts.repository.AccountRepository;
import com.mybank.myaccounts.repository.AccountTypeRepository;
import com.mybank.myaccounts.repository.CustomerRepository;
import com.mybank.myaccounts.service.AccountService;
import com.mybank.myaccounts.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;

/**
 * Implementation of the AccountService interface
 */
@Service
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final CustomerRepository customerRepository;
    private final AccountTypeRepository accountTypeRepository;
    private final AccountMapper accountMapper;
    private final UserService userService;

    @Autowired
    public AccountServiceImpl(
            AccountRepository accountRepository,
            CustomerRepository customerRepository,
            AccountTypeRepository accountTypeRepository,
            AccountMapper accountMapper,
            UserService userService) {
        this.accountRepository = accountRepository;
        this.customerRepository = customerRepository;
        this.accountTypeRepository = accountTypeRepository;
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
    public List<AccountDto> getAccountsWithBalanceGreaterThan(BigDecimal minimumBalance) {
        List<Account> accounts = accountRepository.findAccountsWithBalanceGreaterThan(minimumBalance);
        return accountMapper.toDtoList(accounts);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AccountDto> getAllAccounts(Pageable pageable) {
        Page<Account> accountPage = accountRepository.findAll(pageable);
        return accountPage.map(accountMapper::toDto);
    }

    @Override
    @Transactional
    public AccountDto createAccount(AccountDto accountDto) {
        // Validate required fields
        if (accountDto.getCustomerId() == null) {
            throw new IllegalArgumentException("Customer ID is required");
        }
        
        if (accountDto.getAccountTypeId() == null) {
            throw new IllegalArgumentException("Account type ID is required");
        }
        
        // Check if customer exists
        Customer customer = customerRepository.findById(accountDto.getCustomerId())
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with ID: " + accountDto.getCustomerId()));
        
        // Check if account type exists
        AccountType accountType = accountTypeRepository.findById(accountDto.getAccountTypeId())
                .orElseThrow(() -> new ResourceNotFoundException("Account type not found with ID: " + accountDto.getAccountTypeId()));
        
        // Create new account
        Account account = new Account();
        account.setCustomer(customer);
        account.setAccountType(accountType);
        
        // Generate account number if not provided
        if (accountDto.getAccountNumber() == null || accountDto.getAccountNumber().isEmpty()) {
            account.setAccountNumber(generateAccountNumber());
        } else {
            account.setAccountNumber(accountDto.getAccountNumber());
        }
        
        // Set other fields
        account.setBalance(accountDto.getBalance() != null ? accountDto.getBalance() : BigDecimal.ZERO);
        account.setOpeningDate(accountDto.getOpeningDate() != null ? accountDto.getOpeningDate() : LocalDate.now());
        account.setStatus(accountDto.getStatus() != null ? accountDto.getStatus() : "ACTIVE");
        account.setNotes(accountDto.getNotes());
        account.setCreatedAt(LocalDateTime.now());
        account.setUpdatedAt(LocalDateTime.now());
        
        // Save account
        Account savedAccount = accountRepository.save(account);
        
        // Return DTO
        return accountMapper.toDto(savedAccount);
    }

    @Override
    @Transactional
    public AccountDto updateAccount(Long accountId, AccountDto accountDto) {
        // Find existing account
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found with ID: " + accountId));
        
        // Update fields
        if (accountDto.getAccountNumber() != null) {
            account.setAccountNumber(accountDto.getAccountNumber());
        }
        
        if (accountDto.getStatus() != null) {
            account.setStatus(accountDto.getStatus());
        }
        
        if (accountDto.getNotes() != null) {
            account.setNotes(accountDto.getNotes());
        }
        
        // Update account type if provided
        if (accountDto.getAccountTypeId() != null) {
            AccountType accountType = accountTypeRepository.findById(accountDto.getAccountTypeId())
                    .orElseThrow(() -> new ResourceNotFoundException("Account type not found with ID: " + accountDto.getAccountTypeId()));
            account.setAccountType(accountType);
        }
        
        // Update timestamp
        account.setUpdatedAt(LocalDateTime.now());
        
        // Save account
        Account updatedAccount = accountRepository.save(account);
        
        // Return DTO
        return accountMapper.toDto(updatedAccount);
    }

    @Override
    @Transactional
    public AccountDto updateAccountStatus(Long accountId, String status) {
        // Find existing account
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found with ID: " + accountId));
        
        // Update status
        account.setStatus(status);
        account.setUpdatedAt(LocalDateTime.now());
        
        // Save account
        Account updatedAccount = accountRepository.save(account);
        
        // Return DTO
        return accountMapper.toDto(updatedAccount);
    }

    @Override
    @Transactional
    public boolean closeAccount(Long accountId) {
        // Find existing account
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found with ID: " + accountId));
        
        // Check if account has zero balance
        if (account.getBalance().compareTo(BigDecimal.ZERO) != 0) {
            throw new IllegalStateException("Account must have zero balance to be closed");
        }
        
        // Update status
        account.setStatus("CLOSED");
        account.setUpdatedAt(LocalDateTime.now());
        
        // Save account
        accountRepository.save(account);
        
        return true;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<BigDecimal> getAccountBalance(Long accountId) {
        return accountRepository.findById(accountId)
                .map(Account::getBalance);
    }

    @Override
    @Transactional
    public AccountDto updateAccountBalance(Long accountId, BigDecimal amount) {
        // Find existing account
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found with ID: " + accountId));
        
        // Check if account is active
        if (!"ACTIVE".equals(account.getStatus())) {
            throw new IllegalStateException("Cannot update balance for non-active account");
        }
        
        // Update balance
        BigDecimal newBalance = account.getBalance().add(amount);
        
        // Check if sufficient funds for withdrawal
        if (amount.compareTo(BigDecimal.ZERO) < 0 && newBalance.compareTo(BigDecimal.ZERO) < 0) {
            throw new InsufficientFundsException("Insufficient funds for withdrawal");
        }
        
        account.setBalance(newBalance);
        account.setUpdatedAt(LocalDateTime.now());
        
        // Save account
        Account updatedAccount = accountRepository.save(account);
        
        // Return DTO
        return accountMapper.toDto(updatedAccount);
    }
    
    /**
     * Generate a unique account number
     * 
     * @return a unique account number
     */
    private String generateAccountNumber() {
        // Simple implementation - in production, use a more sophisticated approach
        String prefix = "ACC";
        String timestamp = String.valueOf(System.currentTimeMillis()).substring(5);
        return prefix + timestamp;
    }
    @Override
    public List<Account> findAll() {
        return accountRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Map<String, BigDecimal> getAccountBalancesByUserId(Long userId) {
        // Get customer ID from user ID
        Long customerId = userService.getCustomerIdByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("No customer found for user ID: " + userId));
        
        // Get accounts by customer ID
        List<Account> accounts = accountRepository.findByCustomerCustomerId(customerId);
        
        // Create map of account number to balance
        Map<String, BigDecimal> accountBalances = new HashMap<>();
        for (Account account : accounts) {
            accountBalances.put(account.getAccountNumber(), account.getBalance());
        }
        
        return accountBalances;
    }

    @Override
    @Transactional(readOnly = true)
    public List<AccountBalanceDto> getDetailedAccountBalancesByUserId(Long userId) {
        // Get customer ID from user ID
        Long customerId = userService.getCustomerIdByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("No customer found for user ID: " + userId));
        
        // Get accounts by customer ID
        List<Account> accounts = accountRepository.findByCustomerCustomerId(customerId);
        
        // Create list of account balance DTOs
        List<AccountBalanceDto> accountBalances = new ArrayList<>();
        for (Account account : accounts) {
            AccountBalanceDto balanceDto = AccountBalanceDto.builder()
                    .accountId(account.getAccountId())
                    .accountName(account.getCustomer() != null ? 
                            account.getCustomer().getFirstName() + "'s " + 
                            (account.getAccountType() != null ? account.getAccountType().getName() : "Account") : 
                            "Account")
                    .accountNumber(account.getAccountNumber())
                    .accountType(account.getAccountType() != null ? account.getAccountType().getName() : null)
                    .balance(account.getBalance())
                    .build();
            
            accountBalances.add(balanceDto);
        }
        
        return accountBalances;
    }
}
