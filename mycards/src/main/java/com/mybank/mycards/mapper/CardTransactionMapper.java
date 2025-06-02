package com.mybank.mycards.mapper;

import com.mybank.mycards.dto.CardTransactionDTO;
import com.mybank.common.entity.Card;
import com.mybank.common.entity.CardTransaction;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class CardTransactionMapper {

    /**
     * Converts a CardTransaction entity to a CardTransactionDTO
     */
    public CardTransactionDTO toDTO(CardTransaction transaction) {
        if (transaction == null) {
            return null;
        }

        CardTransactionDTO dto = new CardTransactionDTO();
        dto.setTransactionId(transaction.getTransactionId());

        if (transaction.getCard() != null) {
            dto.setCardId(transaction.getCard().getCardId());
            dto.setCardNumber(transaction.getCard().getCardNumber());
        }

        dto.setAmount(transaction.getAmount());
        dto.setMerchantName(transaction.getMerchantName());
        dto.setMerchantCategory(transaction.getMerchantCategory());
        dto.setTransactionLocation(transaction.getTransactionLocation());
        dto.setTransactionDate(transaction.getTransactionDate());
        dto.setTransactionType(transaction.getTransactionType());
        dto.setStatus(transaction.getStatus());

        return dto;
    }

    /**
     * Creates a new CardTransaction entity from a CardTransactionDTO
     * Note: Card reference must be set separately
     */
    public CardTransaction toEntity(CardTransactionDTO dto) {
        if (dto == null) {
            return null;
        }

        CardTransaction transaction = new CardTransaction();

        // Don't set ID for new entities
        if (dto.getTransactionId() != null) {
            transaction.setTransactionId(dto.getTransactionId());
        }

        transaction.setAmount(dto.getAmount());
        transaction.setMerchantName(dto.getMerchantName());
        transaction.setMerchantCategory(dto.getMerchantCategory());
        transaction.setTransactionLocation(dto.getTransactionLocation());

        // Set transaction date if provided, otherwise use current time
        transaction.setTransactionDate(dto.getTransactionDate() != null ?
                dto.getTransactionDate() : LocalDateTime.now());

        transaction.setTransactionType(dto.getTransactionType());
        transaction.setStatus(dto.getStatus() != null ? dto.getStatus() : "Completed");

        return transaction;
    }

    /**
     * Updates an existing CardTransaction entity with data from a CardTransactionDTO
     */
    public void updateEntityFromDTO(CardTransactionDTO dto, CardTransaction transaction) {
        if (dto == null || transaction == null) {
            return;
        }

        transaction.setAmount(dto.getAmount());
        transaction.setMerchantName(dto.getMerchantName());
        transaction.setMerchantCategory(dto.getMerchantCategory());
        transaction.setTransactionLocation(dto.getTransactionLocation());

        if (dto.getTransactionDate() != null) {
            transaction.setTransactionDate(dto.getTransactionDate());
        }

        transaction.setTransactionType(dto.getTransactionType());

        if (dto.getStatus() != null) {
            transaction.setStatus(dto.getStatus());
        }
    }
}