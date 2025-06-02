package com.mybank.mycards.service.impl;

import com.mybank.common.entity.Card;
import com.mybank.common.entity.CardTransaction;
import com.mybank.common.entity.Customer;
import com.mybank.mycards.dto.CardTransactionDTO;
import com.mybank.mycards.repository.CardRepository;
import com.mybank.mycards.repository.CardTransactionRepository;
import com.mybank.mycards.repository.CustomerRepository;
import com.mybank.mycards.service.ICardTransactionService;
import com.mybank.mycards.service.ICustomerCardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CustomerCardServiceImpl implements ICustomerCardService {

    private final CustomerRepository customerRepository;
    private final CardRepository cardRepository;
    private final CardTransactionRepository cardTransactionRepository;
    private final ICardTransactionService cardTransactionService;

    @Autowired
    public CustomerCardServiceImpl(
            CustomerRepository customerRepository,
            CardRepository cardRepository,
            CardTransactionRepository cardTransactionRepository,
            ICardTransactionService cardTransactionService) {
        this.customerRepository = customerRepository;
        this.cardRepository = cardRepository;
        this.cardTransactionRepository = cardTransactionRepository;
        this.cardTransactionService = cardTransactionService;
    }

    @Override
    public Optional<Customer> findCustomerByPhoneNumber(String phoneNumber) {
        return customerRepository.findByPhoneNumber(phoneNumber);
    }

    @Override
    public List<Card> findCardsByCustomerId(Long customerId) {
        return cardRepository.findByCustomerCustomerId(customerId);
    }

    @Override
    public List<CardTransactionDTO> getRecentTransactionsByCustomer(String phoneNumber, LocalDate date) {
        // Find customer by phone number
        Customer customer = customerRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new RuntimeException("Customer not found with phone number: " + phoneNumber));
        
        // Find all cards for this customer
        List<Card> customerCards = cardRepository.findByCustomerCustomerId(customer.getCustomerId());
        
        if (customerCards.isEmpty()) {
            return new ArrayList<>();
        }
        
        // Calculate date range (7 days from the given date)
        LocalDateTime endOfDay = date.atStartOfDay();
        LocalDateTime startOfDay = date.minusDays(7).atTime(23, 59, 59);
        
        // Collect all transactions for all cards
        List<CardTransaction> allTransactions = new ArrayList<>();
        
        for (Card card : customerCards) {
            System.out.println("Card ID: " + card.getCardId());
            System.out.println("Card Number: " + card.getCardNumber());
            System.out.println("Start of Day: " + startOfDay.toString());
            System.out.println("End of Day: " + endOfDay.toString());

            List<CardTransaction> cardTransactions = 
                cardTransactionRepository.findByCardAndTransactionDateBetween(card, startOfDay, endOfDay);
            allTransactions.addAll(cardTransactions);
        }
        
        // Convert to DTOs
        return allTransactions.stream()
                .map(cardTransactionService::convertToDTO)
                .collect(Collectors.toList());
    }
}