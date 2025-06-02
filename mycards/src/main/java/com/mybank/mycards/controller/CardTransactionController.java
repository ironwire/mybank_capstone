package com.mybank.mycards.controller;

import com.mybank.common.entity.CardTransaction;
import com.mybank.mycards.dto.CardTransactionDTO;
import com.mybank.mycards.service.ICardTransactionService;
import com.mybank.mycards.service.ICustomerCardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/cards/transactions")
public class CardTransactionController {

    private final ICardTransactionService iCardTransactionService;
    private final ICustomerCardService iCustomerCardService;

    public CardTransactionController(ICardTransactionService iCardTransactionService,
            ICustomerCardService iCustomerCardService) {
        this.iCardTransactionService = iCardTransactionService;
        this.iCustomerCardService = iCustomerCardService;
    }

    @PostMapping
    public ResponseEntity<CardTransactionDTO> createCardTransaction(@RequestBody CardTransactionDTO cardTransactionDTO) {
        CardTransactionDTO createdTransaction = iCardTransactionService.createCardTransaction(cardTransactionDTO);
        return new ResponseEntity<>(createdTransaction, HttpStatus.CREATED);
    }



    /**
     * 获取指定用户名在特定日期范围内的卡片交易记录
     *
     *
     * @param startDate 开始日期
     * @param endDate   结束日期
     * @return 卡片交易记录列表
     */
    @GetMapping
    public ResponseEntity<List<CardTransactionDTO>> getTransactions(
            @RequestParam("startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam("endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
            @RequestParam(value = "cardNumber", required = false) String cardNumber) {

        // Convert LocalDate to LocalDateTime for start of day and end of day
        LocalDateTime startOfDay = startDate.atStartOfDay();
        LocalDateTime endOfDay = endDate.atTime(23, 59, 59);
        
        List<CardTransaction> transactions;
        if (cardNumber != null && !cardNumber.isEmpty()) {
            transactions = iCardTransactionService.getTransactionsByCardNumberAndDateRange(cardNumber, startOfDay, endOfDay);
        } else {
            transactions = iCardTransactionService.getTransactionsByUserAndDateRange(startOfDay, endOfDay);
        }
        
        // Convert entities to DTOs to avoid serialization issues
        List<CardTransactionDTO> transactionDTOs = transactions.stream()
                .map(transaction -> iCardTransactionService.convertToDTO(transaction))
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(transactionDTOs);
    }
    @GetMapping("/recent-transactions")
    public ResponseEntity<List<CardTransactionDTO>> getRecentTransactions(
            @RequestParam("phoneNumber") String phoneNumber,
            @RequestParam("date") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) {

        List<CardTransactionDTO> recentTransactions = 
            iCustomerCardService.getRecentTransactionsByCustomer(phoneNumber, date);
        
        return ResponseEntity.ok(recentTransactions);
    }



}
