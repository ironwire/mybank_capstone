package com.mybank.mysettings.controller;

import com.mybank.mysettings.dto.TransactionDto;
import com.mybank.mysettings.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/mysettings/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    @Autowired
    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping
    public ResponseEntity<TransactionDto> createTransaction(@RequestBody TransactionDto transactionDto) {
        TransactionDto createdTransaction = transactionService.createTransaction(transactionDto);
        return new ResponseEntity<>(createdTransaction, HttpStatus.CREATED);
    }
    
    /**
     * Create multiple transactions in a single request
     * 
     * @param transactionDtos list of transaction DTOs to create
     * @return list of created transactions
     */
    @PostMapping("/batch")
    public ResponseEntity<List<TransactionDto>> createTransactions(@RequestBody List<TransactionDto> transactionDtos) {
        List<TransactionDto> createdTransactions = transactionDtos.stream()
                .map(transactionService::createTransaction)
                .collect(Collectors.toList());
        return new ResponseEntity<>(createdTransactions, HttpStatus.CREATED);
    }

    @GetMapping("/{transactionId}")
    public ResponseEntity<TransactionDto> getTransactionById(
            @PathVariable(name = "transactionId") Long transactionId) {
        return transactionService.getTransactionById(transactionId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/account/{accountId}")
    public ResponseEntity<List<TransactionDto>> getTransactionsByAccountId(
            @PathVariable(name = "accountId") Long accountId) {
        List<TransactionDto> transactions = transactionService.getTransactionsByAccountId(accountId);
        return ResponseEntity.ok(transactions);
    }

    @GetMapping("/account/number/{accountNumber}")
    public ResponseEntity<Page<TransactionDto>> getTransactionsByAccountNumber(
            @PathVariable(name = "accountNumber") String accountNumber,
            @PageableDefault(size = 20) Pageable pageable) {
        Page<TransactionDto> transactions = transactionService.getTransactionsByAccountNumber(accountNumber, pageable);
        return ResponseEntity.ok(transactions);
    }

    @GetMapping("/account/{accountId}/date-range")
    public ResponseEntity<List<TransactionDto>> getTransactionsByAccountAndDateRange(
            @PathVariable(name = "accountId") Long accountId,
            @RequestParam(name = "startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(name = "endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        List<TransactionDto> transactions = transactionService.getTransactionsByAccountAndDateRange(
                accountId, startDate, endDate);
        return ResponseEntity.ok(transactions);
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<TransactionDto>> getTransactionsByCustomerId(
            @PathVariable(name = "customerId") Long customerId) {
        List<TransactionDto> transactions = transactionService.getTransactionsByCustomerId(customerId);
        return ResponseEntity.ok(transactions);
    }

    @GetMapping("/type/{transactionType}")
    public ResponseEntity<List<TransactionDto>> getTransactionsByType(
            @PathVariable(name = "transactionType") String transactionType) {
        List<TransactionDto> transactions = transactionService.getTransactionsByType(transactionType);
        return ResponseEntity.ok(transactions);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<TransactionDto>> getTransactionsByUserId(
            @PathVariable(name = "userId") Long userId) {
        List<TransactionDto> transactions = transactionService.getTransactionsByUserId(userId);
        return ResponseEntity.ok(transactions);
    }

    @GetMapping("/user/{userId}/date-range")
    public ResponseEntity<List<TransactionDto>> getTransactionsByUserIdAndDateRange(
            @PathVariable(name = "userId") Long userId,
            @RequestParam(name = "startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(name = "endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        List<TransactionDto> transactions = transactionService.getTransactionsByUserIdAndDateRange(
                userId, startDate, endDate);
        return ResponseEntity.ok(transactions);
    }
}
