package com.mybank.myaccounts.controller;

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
        List<AccountDto>