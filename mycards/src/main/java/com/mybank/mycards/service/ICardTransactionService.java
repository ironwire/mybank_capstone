package com.mybank.mycards.service;

import com.mybank.common.entity.CardTransaction;
import com.mybank.mycards.dto.CardTransactionDTO;

import java.time.LocalDateTime;
import java.util.List;

public interface ICardTransactionService {
    CardTransactionDTO createCardTransaction(CardTransactionDTO cardTransactionDTO);
    List<CardTransaction> getTransactionsByUserAndDateRange(LocalDateTime startDate, LocalDateTime endDate);
    List<CardTransaction> getTransactionsByCardNumberAndDateRange(String cardNumber, LocalDateTime startDate, LocalDateTime endDate);
    CardTransactionDTO convertToDTO(CardTransaction transaction);
}
