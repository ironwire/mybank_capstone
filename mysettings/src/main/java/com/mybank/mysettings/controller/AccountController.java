package com.mybank.mysettings.controller;

import com.mybank.mysettings.dto.AccountDto;
import com.mybank.mysettings.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    private final AccountService accountService;

    @Autowired
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping("/{accountId}")
    public ResponseEntity<AccountDto> getAccountById(
            @PathVariable(name = "accountId") Long accountId) {
        return accountService.getAccountById(accountId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/number/{accountNumber}")
    public ResponseEntity<AccountDto> getAccountByAccountNumber(
            @PathVariable(name = "accountNumber") String accountNumber) {
        return accountService.getAccountByAccountNumber(accountNumber)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<AccountDto>> getAccountsByCustomerId(
            @PathVariable(name = "customerId") Long customerId) {
        List<AccountDto> accounts = accountService.getAccountsByCustomerId(customerId);
        return ResponseEntity.ok(accounts);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<AccountDto>> getAccountsByUserId(
            @PathVariable(name = "userId") Long userId) {
        List<AccountDto> accounts = accountService.getAccountsByUserId(userId);
        return ResponseEntity.ok(accounts);
    }

    @GetMapping("/customer/{customerId}/type/{accountTypeName}")
    public ResponseEntity<List<AccountDto>> getAccountsByCustomerIdAndAccountType(
            @PathVariable(name = "customerId") Long customerId,
            @PathVariable(name = "accountTypeName") String accountTypeName) {
        List<AccountDto> accounts = accountService.getAccountsByCustomerIdAndAccountType(customerId, accountTypeName);
        return ResponseEntity.ok(accounts);
    }

    @GetMapping("/user/{userId}/type/{accountTypeName}")
    public ResponseEntity<List<AccountDto>> getAccountsByUserIdAndAccountType(
            @PathVariable(name = "userId") Long userId,
            @PathVariable(name = "accountTypeName") String accountTypeName) {
        List<AccountDto> accounts = accountService.getAccountsByUserIdAndAccountType(userId, accountTypeName);
        return ResponseEntity.ok(accounts);
    }
}