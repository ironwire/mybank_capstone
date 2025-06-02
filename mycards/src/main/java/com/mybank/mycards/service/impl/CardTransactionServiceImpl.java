package com.mybank.mycards.service.impl;

import com.mybank.mycards.dto.CardTransactionDTO;
import com.mybank.common.entity.Card;
import com.mybank.common.entity.CardTransaction;
import com.mybank.mycards.mapper.CardTransactionMapper;
import com.mybank.mycards.repository.CardRepository;
import com.mybank.mycards.repository.CardTransactionRepository;
import com.mybank.mycards.service.ICardTransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CardTransactionServiceImpl implements ICardTransactionService {

    private final CardTransactionRepository cardTransactionRepository;
    private final CardRepository cardRepository;
    private final CardTransactionMapper cardTransactionMapper;

    @Autowired
    public CardTransactionServiceImpl(CardTransactionRepository cardTransactionRepository,
                                      CardRepository cardRepository,
                                      CardTransactionMapper cardTransactionMapper) {
        this.cardTransactionRepository = cardTransactionRepository;
        this.cardRepository = cardRepository;
        this.cardTransactionMapper = cardTransactionMapper;
    }

    @Override
    @Transactional
    public CardTransactionDTO createCardTransaction(CardTransactionDTO cardTransactionDTO) {
        // Find the card by ID or card number
        Card card = null;
        if (cardTransactionDTO.getCardId() != null) {
            card = cardRepository.findById(cardTransactionDTO.getCardId())
                    .orElseThrow(() -> new RuntimeException("Card not found with ID: " + cardTransactionDTO.getCardId()));
        } else if (cardTransactionDTO.getCardNumber() != null) {
            cardRepository.findByCardNumber(cardTransactionDTO.getCardNumber())
                    .orElseThrow(() -> new RuntimeException("Card not found with number: " + cardTransactionDTO.getCardNumber()));
        } else {
            throw new RuntimeException("Either card ID or card number must be provided");
        }

        // Convert DTO to entity
        CardTransaction transaction = cardTransactionMapper.toEntity(cardTransactionDTO);
        transaction.setCard(card);

        // Save the transaction
        CardTransaction savedTransaction = cardTransactionRepository.save(transaction);

        // Convert back to DTO
        return cardTransactionMapper.toDTO(savedTransaction);
    }

    @Override
    public List<CardTransaction> getTransactionsByUserAndDateRange(
            LocalDateTime startDate,
            LocalDateTime endDate) {

        return cardTransactionRepository.findByUsernameAndTransactionDateBetween(startDate, endDate);
    }

    @Override
    public CardTransactionDTO convertToDTO(CardTransaction transaction) {
        return cardTransactionMapper.toDTO(transaction);
    }

    @Override
    public List<CardTransaction> getTransactionsByCardNumberAndDateRange(
            String cardNumber, LocalDateTime startDate, LocalDateTime endDate) {
        
        // Find the card by card number
        Card card = cardRepository.findByCardNumber(cardNumber)
                .orElseThrow(() -> new RuntimeException("Card not found with number: " + cardNumber));
        
        // Use the repository to find transactions by card and date range
        return cardTransactionRepository.findByCardAndTransactionDateBetween(card, startDate, endDate);
    }
}
