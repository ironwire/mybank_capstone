package com.mybank.myaccounts.controller;

import com.mybank.myaccounts.dto.AccountBalanceDto;
import com.mybank.myaccounts.dto.AccountDto;
import com.mybank.myaccounts.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * REST controller for account operations
 */
@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    private final AccountService accountService;

    @Autowired
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping("/{accountId}")
    public ResponseEntity<AccountDto> getAccountById(@PathVariable Long accountId) {
        return accountService.getAccountById(accountId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/number/{accountNumber}")
    public ResponseEntity<AccountDto> getAccountByAccountNumber(@PathVariable String accountNumber) {
        return accountService.getAccountByAccountNumber(accountNumber)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<AccountDto>> getAccountsByCustomerId(@PathVariable Long customerId) {
        List<AccountDto> accounts = accountService.getAccountsByCustomerId(customerId);
        return ResponseEntity.ok(accounts);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<AccountDto>> getAccountsByUserId(@PathVariable Long userId) {
        List<AccountDto> accounts = accountService.getAccountsByUserId(userId);
        return ResponseEntity.ok(accounts);
    }

    @GetMapping("/customer/{customerId}/type/{accountTypeName}")
    public ResponseEntity<List<AccountDto>> getAccountsByCustomerIdAndAccountType(
            @PathVariable Long customerId,
            @PathVariable String accountTypeName) {
        List<AccountDto> accounts = accountService.getAccountsByCustomerIdAndAccountType(customerId, accountTypeName);
        return ResponseEntity.ok(accounts);
    }

    @GetMapping("/balance")
    public ResponseEntity<List<AccountDto>> getAccountsWithBalanceGreaterThan(
            @RequestParam BigDecimal minimumBalance) {
        List<AccountDto> accounts = accountService.getAccountsWithBalanceGreaterThan(minimumBalance);
        return ResponseEntity.ok(accounts);
    }

    @GetMapping
    public ResponseEntity<Page<AccountDto>> getAllAccounts(Pageable pageable) {
        Page<AccountDto> accounts = accountService.getAllAccounts(pageable);
        return ResponseEntity.ok(accounts);
    }

    @PostMapping
    public ResponseEntity<AccountDto> createAccount(@RequestBody AccountDto accountDto) {
        AccountDto createdAccount = accountService.createAccount(accountDto);
        return new ResponseEntity<>(createdAccount, HttpStatus.CREATED);
    }

    @PutMapping("/{accountId}")
    public ResponseEntity<AccountDto> updateAccount(
            @PathVariable Long accountId,
            @RequestBody AccountDto accountDto) {
        AccountDto updatedAccount = accountService.updateAccount(accountId, accountDto);
        return ResponseEntity.ok(updatedAccount);
    }

    @PatchMapping("/{accountId}/status")
    public ResponseEntity<AccountDto> updateAccountStatus(
            @PathVariable Long accountId,
            @RequestParam String status) {
        AccountDto updatedAccount = accountService.updateAccountStatus(accountId, status);
        return ResponseEntity.ok(updatedAccount);
    }

    @PostMapping("/{accountId}/close")
    public ResponseEntity<Void> closeAccount(@PathVariable Long accountId) {
        boolean closed = accountService.closeAccount(accountId);
        return closed ? ResponseEntity.ok().build() : ResponseEntity.badRequest().build();
    }

    @GetMapping("/{accountId}/balance")
    public ResponseEntity<BigDecimal> getAccountBalance(@PathVariable Long accountId) {
        return accountService.getAccountBalance(accountId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/{accountId}/balance")
    public ResponseEntity<AccountDto> updateAccountBalance(
            @PathVariable Long accountId,
            @RequestParam BigDecimal amount) {
        AccountDto updatedAccount = accountService.updateAccountBalance(accountId, amount);
        return ResponseEntity.ok(updatedAccount);
    }

    @GetMapping("/user/{userId}/balances")
    public ResponseEntity<List<AccountBalanceDto>> getAccountBalancesByUserId(@PathVariable(name = "userId") Long userId) {
        List<AccountBalanceDto> accountBalances = accountService.getDetailedAccountBalancesByUserId(userId);
        return ResponseEntity.ok(accountBalances);
    }
}
