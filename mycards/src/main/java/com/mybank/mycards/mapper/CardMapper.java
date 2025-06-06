package com.mybank.mycards.mapper;

import com.mybank.common.entity.Card;
import com.mybank.mycards.dto.CardDTO;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CardMapper {

    public CardDTO toDTO(Card card) {
        if (card == null) {
            return null;
        }

        return CardDTO.builder()
                .cardId(card.getCardId())
                .customerId(card.getCustomer() != null ? card.getCustomer().getCustomerId() : null)
                .accountId(card.getAccountId())
                .cardTypeName(card.getCardType() != null ? card.getCardType().getName() : null)
                .cardNumber(card.getCardNumber())
                .cardholderName(card.getCardholderName())
                .expiryDate(card.getExpiryDate())
                .limitAmount(card.getLimitAmount())
                .statusName(card.getStatusName())
                .billingAddress(card.getBillingAddress())
                .billingCity(card.getBillingCity())
                .billingState(card.getBillingState())
                .billingZip(card.getBillingZip())
                .issueDate(card.getIssueDate())
                .build();
    }

    public List<CardDTO> toDTOList(List<Card> cards) {
        if (cards == null) {
            return null;
        }
        
        return cards.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
}